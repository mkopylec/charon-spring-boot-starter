package com.github.mkopylec.charon.interceptors.rewrite;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.interceptors.RequestForwardingException.requestForwardingErrorIf;
import static java.util.regex.Pattern.compile;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

class RegexRequestPathRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RegexRequestPathRewriter.class);

    private Pattern incomingRequestPathRegex;
    private PathTemplate outgoingRequestPathTemplate;

    RegexRequestPathRewriter() {
        incomingRequestPathRegex = compile("/(?<path>.*)");
        outgoingRequestPathTemplate = new PathTemplate("/<path>");
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Regex request path rewriting for '{}' request mapping", execution.getMappingName());
        rewritePath(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Regex request path rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public void validate() {
        isTrue(!outgoingRequestPathTemplate.isEmpty(), "No outgoing request path template set");
    }

    @Override
    public int getOrder() {
        return REQUEST_PATH_REWRITER_ORDER;
    }

    void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = new PathTemplate(outgoingRequestPathTemplate);
    }

    private void rewritePath(HttpRequest request) {
        URI requestUri = request.getOriginalUri();
        String requestPath = requestUri.getPath();
        Matcher matcher = incomingRequestPathRegex.matcher(requestPath);
        requestForwardingErrorIf(!matcher.find(), "Incoming request path " + requestPath + " does not match path rewriter regex pattern " + incomingRequestPathRegex);
        String rewrittenRequestPath = outgoingRequestPathTemplate.fill(matcher);
        URI rewrittenRequestUri = fromUri(requestUri)
                .replacePath(rewrittenRequestPath)
                .build(true)
                .toUri();
        request.setUri(rewrittenRequestUri);
        log.debug("Request path rewritten from {} to {}", requestPath, rewrittenRequestPath);
    }
}
