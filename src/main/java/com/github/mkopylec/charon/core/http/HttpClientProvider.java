package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HttpClientProvider {

    protected Map<String, RestTemplate> httpClients = new HashMap<>();

    public void updateHttpClients(List<MappingProperties> mappings) {
        httpClients = mappings.stream().collect(toMap(MappingProperties::getName, this::createRestTemplate));
    }

    public RestTemplate getHttpClient(String mappingName) {
        return httpClients.get(mappingName);
    }

    /**
     * Can be overriden to modify the RestTemplate
     *
     * @param mapping
     * @return
     */
    protected RestTemplate createRestTemplate(MappingProperties mapping) {
        CloseableHttpClient client = createHttpClient(mapping).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        requestFactory.setConnectTimeout(mapping.getTimeout().getConnect());
        requestFactory.setReadTimeout(mapping.getTimeout().getRead());
        return new RestTemplate(requestFactory);
    }

    /**
     * Can be overriden to modify the HttpClient
     *
     * @param mapping
     * @return
     */
    protected HttpClientBuilder createHttpClient(MappingProperties mapping/*can be used by overriden methods*/) {
        final HttpClientBuilder builder = HttpClientBuilder.create();
        builder
                .useSystemProperties()
                // Don't stock the cookies that are sent between the client and the browser
                // This can cause a lot of problems around mixing Sessions between HTTP connections
                // This can lead to Session steal from a user to another!
                // Hint: Spring Cloud Hystrix also disables it by default
                .disableCookieManagement()
                // Tells HttpClient to not follow "302 Redirect" but to send them bac to the *browser*
                .disableRedirectHandling();
        return builder;
    }
}
