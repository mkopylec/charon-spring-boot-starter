package com.github.mkopylec.charon.test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static java.lang.String.valueOf;
import static org.apache.commons.io.IOUtils.toByteArray;

class RetryAttemptsResponseHeaderSetter implements ClientHttpRequestInterceptor {

    private AtomicInteger attempt = new AtomicInteger(); // Works only because of @DirtiesContext

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);
        HttpResponse httpResponse = new HttpResponse(response.getStatusCode());
        HttpHeaders headers = copyHeaders(response.getHeaders());
        headers.set("Retry-Attempts", valueOf(attempt.incrementAndGet()));
        httpResponse.setHeaders(headers);
        httpResponse.setBody(toByteArray(response.getBody()));
        return httpResponse;
    }
}
