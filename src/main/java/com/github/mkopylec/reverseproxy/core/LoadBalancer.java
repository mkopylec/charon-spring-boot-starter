package com.github.mkopylec.reverseproxy.core;

import java.util.List;

public interface LoadBalancer {

	String chooseDestination(List<String> destination);
}
