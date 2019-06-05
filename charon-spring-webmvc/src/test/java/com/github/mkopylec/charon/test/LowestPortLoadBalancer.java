package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.rewrite.LoadBalancer;

import java.net.URI;
import java.util.List;

import static java.util.Comparator.comparingInt;

class LowestPortLoadBalancer implements LoadBalancer {

    @Override
    public URI chooseServer(List<URI> servers) {
        return servers.stream()
                .min(comparingInt(URI::getPort))
                .get();
    }
}
