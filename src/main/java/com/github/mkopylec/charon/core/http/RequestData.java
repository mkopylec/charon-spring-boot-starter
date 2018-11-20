package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.github.mkopylec.charon.core.utils.BodyConverter.convertStringToBody;

public class RequestData extends UnmodifiableRequestData {

    public RequestData(MappingProperties mapping, HttpMethod method, String uri, HttpHeaders headers, byte[] body) {
        super(mapping, method, uri, headers, body);
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
