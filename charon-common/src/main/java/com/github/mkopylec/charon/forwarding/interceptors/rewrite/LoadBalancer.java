package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Valid;

import java.net.URI;
import java.util.List;

public interface LoadBalancer extends Valid {

    URI chooseServer(List<URI> servers, BodilessHttpRequest request);
}
