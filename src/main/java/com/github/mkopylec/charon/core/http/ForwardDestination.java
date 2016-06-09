package com.github.mkopylec.charon.core.http;

import java.net.URI;

public class ForwardDestination {

    protected final URI uri;
    protected final String mappingMetricsName;

    public ForwardDestination(URI uri, String mappingMetricsName) {
        this.uri = uri;
        this.mappingMetricsName = mappingMetricsName;
    }

    public URI getUri() {
        return uri;
    }

    public String getMappingMetricsName() {
        return mappingMetricsName;
    }
}
