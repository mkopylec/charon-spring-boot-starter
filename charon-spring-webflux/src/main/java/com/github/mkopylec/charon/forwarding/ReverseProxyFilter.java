package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import org.springframework.boot.web.reactive.filter.OrderedWebFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class ReverseProxyFilter implements OrderedWebFilter {

    private int order;
    private RequestMappingResolver requestMappingResolver;
    private HttpRequestMapper httpRequestMapper;
    private WebClientProvider webClientProvider;
    private HttpResponseMapper httpResponseMapper;

    public ReverseProxyFilter(int order, List<RequestMappingConfiguration> requestMappingConfigurations) {
        this.order = order;
        requestMappingResolver = new RequestMappingResolver(requestMappingConfigurations);
        httpRequestMapper = new HttpRequestMapper();
        webClientProvider = new WebClientProvider();
        httpResponseMapper = new HttpResponseMapper();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        RequestMappingConfiguration configuration = requestMappingResolver.resolveRequestMapping(request);
        if (configuration == null) {
            return chain.filter(exchange);
        }
        WebClient webClient = webClientProvider.getWebClient(configuration);
        return webClient.method(httpRequestMapper.extractMethod(request))
                .uri(httpRequestMapper.extractUri(request))
                .headers(headers -> headers.putAll(httpRequestMapper.extractHeaders(request)))
                .body(httpRequestMapper.extractBody(request))
                .exchangeToMono(clientResponse -> httpResponseMapper.map(clientResponse, response));
    }
}
