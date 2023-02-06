package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Valid;
import org.springframework.http.client.reactive.ClientHttpConnector;

public interface ClientHttpConnectorCreator extends Valid {

    ClientHttpConnector createConnector(TimeoutConfiguration configuration);
}
