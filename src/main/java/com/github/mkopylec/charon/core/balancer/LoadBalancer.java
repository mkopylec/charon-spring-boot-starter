package com.github.mkopylec.charon.core.balancer;

import java.util.List;

public interface LoadBalancer {

    String chooseDestination(List<String> destinations);
}
