package com.github.mkopylec.charon.forwarding;

import org.springframework.http.client.reactive.ClientHttpConnector;

public interface ClientHttpConnectorCreator {

    ClientHttpConnector createConnector(TimeoutConfiguration configuration);
}
