package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static org.springframework.util.Assert.notNull;
import static reactor.core.publisher.Mono.fromRunnable;
import static reactor.core.publisher.Mono.just;

abstract class Authenticator<V extends CredentialsValidator> extends CommonAuthenticator implements RequestForwardingInterceptor {

    private V credentialsValidator;

    Authenticator(Logger log, AuthenticationType authenticationType) {
        super(log, authenticationType);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        String credentials = extractCredentials(request.headers());
        if (credentials == null) {
            return just(getFailedAuthenticationResponse(request));
        }
        return credentialsValidator.validate(credentials)
                .filter(authenticated -> !authenticated)
                .map(notAuthenticated -> getFailedAuthenticationResponse(request))
                .switchIfEmpty(fromRunnable(() -> getLog().debug("Authentication successful"))
                        .then(execution.execute(request)))
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }

    @Override
    public void validate() {
        notNull(credentialsValidator, "No credentials validator set");
        credentialsValidator.validate();
        super.validate();
    }

    void setCredentialsValidator(V credentialsValidator) {
        this.credentialsValidator = credentialsValidator;
    }

    private HttpResponse getFailedAuthenticationResponse(HttpRequest request) {
        HttpResponse response = new HttpResponse(getAuthenticationFailureResponseStatus(), request);
        setAuthenticationInformation(response.headers().asHttpHeaders(), response::setHeaders);
        return response;
    }
}
