package com.github.mkopylec.charon.forwarding;

import reactor.netty.http.client.HttpClient;

public class ReactorClientHttpConnectorCreatorConfigurer extends ClientHttpConnectorCreatorConfigurer<ReactorClientHttpConnectorCreator> {

    private ReactorClientHttpConnectorCreatorConfigurer() {
        super(new ReactorClientHttpConnectorCreator());
    }

    public static ReactorClientHttpConnectorCreatorConfigurer reactorClientHttpConnectorCreator() {
        return new ReactorClientHttpConnectorCreatorConfigurer();
    }

    // TODO doc
    public ReactorClientHttpConnectorCreatorConfigurer httpClient(HttpClient httpClient) {
        configuredObject.setHttpClient(httpClient);
        return this;
    }
}
