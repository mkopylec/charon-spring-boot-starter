package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HttpClientProvider {

    protected final CharonProperties charon;
    protected Map<String, RestOperations> httpClients = new HashMap<>();

    public HttpClientProvider(CharonProperties charon) {
        this.charon = charon;
    }

    public void updateHttpClients() {
        httpClients = charon.getMappings().stream().collect(toMap(MappingProperties::getName, this::createHttpClient));
    }

    public RestOperations getHttpClient(String mappingName) {
        return httpClients.get(mappingName);
    }

    protected RestOperations createHttpClient(MappingProperties mapping) {
        Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(mapping.getTimeout().getConnect());
        requestFactory.setReadTimeout(mapping.getTimeout().getRead());
        return new RestTemplate(requestFactory);
    }
}
