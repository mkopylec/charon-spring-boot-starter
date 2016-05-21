package com.github.mkopylec.reverseproxy.core;

import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

public class RandomLoadBalancer implements LoadBalancer {

	@Override
	public String chooseHost(List<String> hosts) {
		int hostIndex = hosts.size() == 1 ? 0 : current().nextInt(0, hosts.size());
		return hosts.get(hostIndex);
	}
}
