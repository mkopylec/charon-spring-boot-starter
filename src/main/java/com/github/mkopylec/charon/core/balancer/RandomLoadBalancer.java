package com.github.mkopylec.charon.core.balancer;

import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public String chooseDestination(List<String> destinations) {
        int hostIndex = destinations.size() == 1 ? 0 : current().nextInt(0, destinations.size());
        return destinations.get(hostIndex);
    }
}
