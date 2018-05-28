package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.github.mkopylec.charon.core.utils.BodyConverter.convertStringToBody;

public class RequestData extends UnmodifiableRequestData {

    public RequestData(HttpMethod method, String uri, HttpHeaders headers, byte[] body) {
        super(method, uri, headers, body);
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setBody(String body) {
        this.body = convertStringToBody(body);
    }
}
