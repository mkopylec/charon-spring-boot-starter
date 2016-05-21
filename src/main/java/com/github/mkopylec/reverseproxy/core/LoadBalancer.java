package com.github.mkopylec.reverseproxy.core;

import java.util.List;

public interface LoadBalancer {

	String chooseHost(List<String> hosts);
}
