package com.github.mkopylec.reverseproxy.core;

import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

public class RandomLoadBalancer implements LoadBalancer {

	@Override
	public String chooseDestination(List<String> destination) {
		int hostIndex = destination.size() == 1 ? 0 : current().nextInt(0, destination.size());
		return destination.get(hostIndex);
	}
}
