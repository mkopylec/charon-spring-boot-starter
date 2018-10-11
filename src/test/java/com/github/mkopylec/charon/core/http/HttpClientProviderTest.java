package com.github.mkopylec.charon.core.http;

import com.github.mkopylec.charon.configuration.MappingProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpClientProviderTest {

    @Test
    public void updateHttpClients_createRestTemplate_createHttpClient_Called() {
        final List<MappingProperties> mappings = new ArrayList<>();
        final MappingProperties mp = new MappingProperties();
        mp.setName("the-route");
        mappings.add(mp);
        final AtomicBoolean rtCalled = new AtomicBoolean(false);
        final AtomicBoolean hcCalled = new AtomicBoolean(false);
        final HttpClientProvider p = new HttpClientProvider() {
            @Override
            protected RestTemplate createRestTemplate(MappingProperties mapping) {
                rtCalled.set(true);
                return super.createRestTemplate(mapping);
            }

            @Override
            protected HttpClientBuilder createHttpClient(MappingProperties mapping) {
                hcCalled.set(true);
                return super.createHttpClient(mapping);
            }
        };
        p.updateHttpClients(mappings);
        Assert.assertEquals(1, p.httpClients.size());
        Assert.assertTrue(rtCalled.get());
        Assert.assertTrue(hcCalled.get());
    }
}
