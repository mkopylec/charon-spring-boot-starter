package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.springframework.util.Assert.notNull;

abstract class Authenticator<V extends CredentialsValidator> extends CommonAuthenticator implements RequestForwardingInterceptor {

    private V credentialsValidator;

    Authenticator(Logger log, AuthenticationType authenticationType) {
        super(log, authenticationType);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        String credentials = extractCredentials(request.getHeaders());
        if (credentials == null) {
            return getFailedAuthenticationResponse();
        }
        boolean authenticated = credentialsValidator.validate(credentials);
        if (!authenticated) {
            return getFailedAuthenticationResponse();
        }
        getLog().debug("Authentication successful");
        HttpResponse response = execution.execute(request);
        logEnd(execution.getMappingName());
        return response;
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

    private HttpResponse getFailedAuthenticationResponse() {
        HttpResponse response = new HttpResponse(getAuthenticationFailureResponseStatus());
        setAuthenticationInformation(response.getHeaders(), response::setHeaders);
        return response;
    }
}
