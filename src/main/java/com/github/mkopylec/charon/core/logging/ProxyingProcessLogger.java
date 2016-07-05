package com.github.mkopylec.charon.core.logging;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.core.http.ForwardDestination;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static com.github.mkopylec.charon.configuration.CharonProperties.LoggingMode.SIMPLE;
import static java.nio.charset.Charset.forName;
import static org.slf4j.LoggerFactory.getLogger;

public class ProxyingProcessLogger {

    private static final Logger log = getLogger(ProxyingProcessLogger.class);

    protected final CharonProperties charon;

    public ProxyingProcessLogger(CharonProperties charon) {
        this.charon = charon;
    }

    public void logIncomingRequest(HttpMethod method, String uri, byte[] body, HttpHeaders headers) {
        if (isSimpleLoggingOn()) {
            log.trace("Incoming: {} {}", method, uri);
        } else {
            log.trace("\n  Incoming HTTP request details:\n    - method: {}\n    - uri: {}\n    - body: {}\n    - headers: {}", method, uri, convertBodyToString(body), headers);
        }
    }

    public void logForwardingProcess(HttpMethod method, String originUri, byte[] requestBody, HttpHeaders requestHeaders) {
        if (isSimpleLoggingOn()) {
            log.trace("Forwarding: {} {} -> no mapping found", method, originUri);
        } else {
            log.trace("\n  Forwarding process details:\n    - mapping name: <no mapping found for incoming HTTP request>\n    - request method: {}\n    - origin uri: {}\n    - request body: {}\n    - request headers: {}",
                    method, originUri, convertBodyToString(requestBody), requestHeaders
            );
        }
    }

    public void logForwardingProcess(HttpMethod method, String originUri, byte[] requestBody, HttpHeaders requestHeaders, ForwardDestination destination, ResponseEntity<byte[]> response) {
        if (isSimpleLoggingOn()) {
            log.debug("Forwarding: {} {} -> {} {}", method, originUri, destination.getUri(), response.getStatusCode().value());
        } else {
            log.debug("\n  Forwarding process details:\n    - mapping name: {}\n    - request method: {}\n    - origin uri: {}\n    - forward destination: {}\n    - forward request body: {}\n    - forward headers: {}\n    - response status: {}\n    - response body: {}\n    - response headers: {}",
                    destination.getMappingName(), method, originUri, destination.getUri(), convertBodyToString(requestBody), requestHeaders, response.getStatusCode().value(), convertBodyToString(response.getBody()), response.getHeaders()
            );
        }
    }

    protected boolean isSimpleLoggingOn() {
        return charon.getLoggingMode() == SIMPLE;
    }

    protected String convertBodyToString(byte[] body) {
        if (body == null) {
            return null;
        }
        return new String(body, forName("UTF-8"));
    }
}
