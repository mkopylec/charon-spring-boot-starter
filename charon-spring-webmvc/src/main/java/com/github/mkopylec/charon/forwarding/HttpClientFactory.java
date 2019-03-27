package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.utils.Valid;

import org.springframework.web.client.RestTemplate;

public interface HttpClientFactory extends Valid {

    RestTemplate createHttpClient(RequestForwarding forwarding);
}
