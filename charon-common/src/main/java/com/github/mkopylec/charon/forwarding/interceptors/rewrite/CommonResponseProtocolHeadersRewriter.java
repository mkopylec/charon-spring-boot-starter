package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;

import java.util.function.Consumer;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_PROTOCOL_HEADERS_REWRITER;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.SERVER;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;

abstract class CommonResponseProtocolHeadersRewriter implements Valid {

    private static final String PUBLIC_KEY_PINS = "Public-Key-Pins";
    private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    private Logger log;

    CommonResponseProtocolHeadersRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return RESPONSE_PROTOCOL_HEADERS_REWRITER;
    }

    void rewriteHeaders(HttpHeaders headers, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.remove(TRANSFER_ENCODING);
        rewrittenHeaders.remove(CONNECTION);
        rewrittenHeaders.remove(PUBLIC_KEY_PINS);
        rewrittenHeaders.remove(SERVER);
        rewrittenHeaders.remove(STRICT_TRANSPORT_SECURITY);
        headersSetter.accept(rewrittenHeaders);
        log.debug("Response headers rewritten from {} to {}", headers, rewrittenHeaders);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Response protocol headers rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Response protocol headers rewriting for '{}' request mapping", mappingName);
    }
}
