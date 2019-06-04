package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_PROTOCOL_HEADERS_REWRITER;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.SERVER;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;

abstract class BasicResponseProtocolHeadersRewriter implements Ordered, Valid {

    private static final String PUBLIC_KEY_PINS = "Public-Key-Pins";
    private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    private Logger log;

    BasicResponseProtocolHeadersRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return RESPONSE_PROTOCOL_HEADERS_REWRITER.getOrder();
    }

    void rewriteHeaders(HttpHeaders headers) {
        HttpHeaders oldHeaders = copyHeaders(headers);
        headers.remove(TRANSFER_ENCODING);
        headers.remove(CONNECTION);
        headers.remove(PUBLIC_KEY_PINS);
        headers.remove(SERVER);
        headers.remove(STRICT_TRANSPORT_SECURITY);
        log.debug("Response headers rewritten from {} to {}", oldHeaders, headers);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Response protocol headers rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Response protocol headers rewriting for '{}' request mapping", mappingName);
    }
}
