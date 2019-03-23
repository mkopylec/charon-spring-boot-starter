package com.github.mkopylec.charon.core.interceptors;

import java.net.URI;
import java.util.List;

public interface LoadBalancer {

    URI chooseServer(List<URI> servers);

    default void validate() {

    }
}
