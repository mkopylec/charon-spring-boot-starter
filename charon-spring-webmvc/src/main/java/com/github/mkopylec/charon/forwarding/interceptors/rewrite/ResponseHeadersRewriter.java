package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.HeadersUtils.copyHeaders;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.SERVER;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;

class ResponseHeadersRewriter implements RequestForwardingInterceptor {

    private static final String PUBLIC_KEY_PINS = "Public-Key-Pins";
    private static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";

    private static final Logger log = getLogger(ResponseHeadersRewriter.class);

    ResponseHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Response headers rewriting for '{}' request mapping", execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteHeaders(response);
        log.trace("[End] Response headers rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_HEADERS_REWRITER_ORDER;
    }

    private void rewriteHeaders(HttpResponse response) {
        HttpHeaders oldHeaders = copyHeaders(response.getHeaders());
        HttpHeaders headers = response.getHeaders();
        headers.remove(TRANSFER_ENCODING);
        headers.remove(CONNECTION);
        headers.remove(PUBLIC_KEY_PINS);
        headers.remove(SERVER);
        headers.remove(STRICT_TRANSPORT_SECURITY);
        log.debug("Response headers rewritten from {} to {}", oldHeaders, headers);
    }
}
