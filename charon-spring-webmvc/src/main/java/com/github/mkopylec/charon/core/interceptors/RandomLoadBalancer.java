package com.github.mkopylec.charon.core.interceptors;

import java.net.URI;
import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public URI chooseServer(List<URI> servers) {
        int index = servers.size() == 1 ? 0 : current().nextInt(0, servers.size());
        return servers.get(index);
    }
}
