package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.replaceFirst;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class MultipartHeaderFixer implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String correctedContentType = replaceFirst(headers.getFirst(CONTENT_TYPE), MULTIPART_FORM_DATA_VALUE + ";", MULTIPART_FORM_DATA_VALUE + "; ");
        headers.set(CONTENT_TYPE, correctedContentType);
        return execution.execute(request, body);
    }
}
