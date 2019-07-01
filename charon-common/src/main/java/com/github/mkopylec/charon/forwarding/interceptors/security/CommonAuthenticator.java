package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.AUTHENTICATOR;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.removeStartIgnoreCase;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.Assert.isTrue;

abstract class CommonAuthenticator implements Valid {

    private Logger log;
    private AuthenticationType authenticationType;
    private String realm;

    CommonAuthenticator(Logger log, AuthenticationType authenticationType) {
        this.log = log;
        this.authenticationType = authenticationType;
        this.realm = "Charon security";
    }

    @Override
    public void validate() {
        isTrue(isNotBlank(realm), "No authentication realm set");
    }

    public RequestForwardingInterceptorType getType() {
        return AUTHENTICATOR;
    }

    void setRealm(String realm) {
        this.realm = realm;
    }

    HttpStatus getAuthenticationFailureResponseStatus() {
        return UNAUTHORIZED;
    }

    String extractCredentials(HttpHeaders requestHeaders) {
        String authorization = requestHeaders.getFirst(AUTHORIZATION);
        if (isBlank(authorization)) {
            log.debug("No 'Authorization' request header found");
            return null;
        }
        return removeStartIgnoreCase(authorization.trim(), authenticationType.toString()).trim();
    }

    void setAuthenticationInformation(HttpHeaders responseHeaders, Consumer<HttpHeaders> responseHeadersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(responseHeaders);
        rewrittenHeaders.set(WWW_AUTHENTICATE, authenticationType + " realm=\"" + realm + "\"");
        responseHeadersSetter.accept(rewrittenHeaders);
        log.debug("Response headers rewritten from {} to {}", responseHeaders, rewrittenHeaders);
    }

    void logStart(String mappingName) {
        log.trace("[Start] {} authentication for '{}' request mapping", authenticationType, mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] {} authentication for '{}' request mapping", authenticationType, mappingName);
    }
}
