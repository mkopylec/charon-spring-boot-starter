package com.github.mkopylec.charon.forwarding;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;
import org.slf4j.Logger;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;

class RequestForwardingResolver {

    private static final Logger log = getLogger(RequestForwardingResolver.class);

    private List<RequestForwardingConfiguration> requestForwardingConfigurations;

    RequestForwardingResolver(List<RequestForwardingConfiguration> requestForwardingConfigurations) {
        this.requestForwardingConfigurations = requestForwardingConfigurations;
    }

    RequestForwardingConfiguration resolveRequestForwarding(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        List<RequestForwardingConfiguration> configurations = requestForwardingConfigurations.stream()
                .filter(configuration -> configuration.getPathRegex().matcher(requestURI).matches())
                .collect(toList());
        if (configurations.isEmpty()) {
            log.debug("No request forwarding matches {} incoming request", requestURI);
            return null;
        }
        isTrue(configurations.size() == 1, () -> "More than one request forwarding matches "
                + requestURI + " incoming request. Matching forwardings are "
                + configurations.stream().map(RequestForwardingConfiguration::toString).collect(joining(", ")));
        RequestForwardingConfiguration configuration = configurations.get(0);
        log.debug("Request forwarding {} matches {} incoming request", configuration, requestURI);
        return configuration;
    }
}
