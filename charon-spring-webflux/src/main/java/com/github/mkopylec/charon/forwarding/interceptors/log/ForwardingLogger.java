package com.github.mkopylec.charon.forwarding.interceptors.log;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;

import static org.slf4j.LoggerFactory.getLogger;

class ForwardingLogger extends BasicForwardingLogger implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ForwardingLogger.class);

    ForwardingLogger() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        HttpMethod oldMethod = request.method();
        URI oldUri = request.url();
        String forwardingName = execution.getMappingName();
        return execution.execute(request)
                .doOnSuccess(response -> {
                    String logMessage = "Forwarding: {} {} -> '{}' -> {} {} {}";
                    if (response.statusCode().is5xxServerError()) {
                        log(serverErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.method(), request.url(), response.rawStatusCode());
                    } else if (response.statusCode().is4xxClientError()) {
                        log(clientErrorLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.method(), request.url(), response.rawStatusCode());
                    } else {
                        log(successLogLevel, logMessage, oldMethod, oldUri, forwardingName, request.method(), request.url(), response.rawStatusCode());
                    }
                })
                .doOnError(RuntimeException.class, e -> {
                    log(unexpectedErrorLogLevel, "Forwarding: {} {} -> '{}' -> {}", oldMethod, oldUri, forwardingName, e.getMessage());
                    throw e;
                });
    }
}
