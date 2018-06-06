package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.apache.http.impl.client.HttpClientBuilder.create;

public class HttpClientProvider {

    protected Map<String, RestOperations> httpClients = new HashMap<>();

    public void updateHttpClients(List<MappingProperties> mappings) {
        httpClients = mappings.stream().collect(toMap(MappingProperties::getName, this::createHttpClient));
    }

    public RestOperations getHttpClient(String mappingName) {
        return httpClients.get(mappingName);
    }

    protected RestOperations createHttpClient(MappingProperties mapping) {
        CloseableHttpClient client = create().useSystemProperties().disableRedirectHandling().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        requestFactory.setConnectTimeout(mapping.getTimeout().getConnect());
        requestFactory.setReadTimeout(mapping.getTimeout().getRead());
        return new RestTemplate(requestFactory);
    }
}
