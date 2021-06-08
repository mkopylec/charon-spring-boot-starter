package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.CommonHttpRequest;

public interface LoadBalancer extends Valid {

    URI chooseServer(List<URI> servers);

    default URI chooseServer(CommonHttpRequest request, List<URI> servers) {
        return chooseServer(servers);
    }
}
