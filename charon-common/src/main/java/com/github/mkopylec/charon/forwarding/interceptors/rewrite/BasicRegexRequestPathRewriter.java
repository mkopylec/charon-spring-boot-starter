package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;
import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingErrorIf;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PATH_REWRITER;
import static java.util.regex.Pattern.compile;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

abstract class BasicRegexRequestPathRewriter implements Valid {

    private Logger log;
    private Pattern incomingRequestPathRegex;
    private PathTemplate outgoingRequestPathTemplate;

    BasicRegexRequestPathRewriter(Logger log) {
        this.log = log;
        incomingRequestPathRegex = compile("/(?<path>.*)");
        outgoingRequestPathTemplate = new PathTemplate("/<path>");
    }

    public RequestForwardingInterceptorType getType() {
        return REQUEST_PATH_REWRITER;
    }

    void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = new PathTemplate(outgoingRequestPathTemplate);
    }

    void rewritePath(URI uri, Consumer<URI> rewrittenUriSetter) {
        String requestPath = uri.getPath();
        Matcher matcher = incomingRequestPathRegex.matcher(requestPath);
        requestForwardingErrorIf(!matcher.find(), () -> "Incoming request path " + requestPath + " does not match path rewriter regex pattern " + incomingRequestPathRegex);
        String rewrittenPath;
        try {
            rewrittenPath = outgoingRequestPathTemplate.fill(matcher);
        } catch (IllegalArgumentException e) {
            throw requestForwardingError("Path rewriter regex pattern " + incomingRequestPathRegex + " does not contain groups required to fill request path template " + outgoingRequestPathTemplate, e);
        }
        URI rewrittenUri = fromUri(uri)
                .replacePath(rewrittenPath)
                .build(true)
                .toUri();
        rewrittenUriSetter.accept(rewrittenUri);
        log.debug("Request path rewritten from {} to {}", requestPath, rewrittenPath);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Regex request path rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Regex request path rewriting for '{}' request mapping", mappingName);
    }
}
