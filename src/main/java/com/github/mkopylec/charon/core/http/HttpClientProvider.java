package com.github.mkopylec.charon.core.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mkopylec.charon.configuration.MappingProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

import static java.util.stream.Collectors.toMap;
import static org.apache.http.client.config.RequestConfig.custom;
import static org.apache.http.impl.client.HttpClientBuilder.create;

public class HttpClientProvider {

    protected Map<String, CloseableHttpClient> httpClients = new HashMap<>();

    public void updateHttpClients(List<MappingProperties> mappings) {
        httpClients = mappings.stream().collect(toMap(MappingProperties::getName, this::createHttpClient));
    }

    public CloseableHttpClient getHttpClient(String mappingName) {
        return httpClients.get(mappingName);
    }

    protected CloseableHttpClient createHttpClient(MappingProperties mapping) {
        RequestConfig config = custom()
                .setConnectTimeout(mapping.getTimeout().getConnect())
                .setSocketTimeout(mapping.getTimeout().getSocket())
                .build();
        return create().useSystemProperties()
                .disableRedirectHandling()
                .setDefaultRequestConfig(config)
                .build();
    }
}
