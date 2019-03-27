package com.github.mkopylec.charon.forwarding;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

public class CharonProxyFilter extends OncePerRequestFilter implements Ordered {

    private int order;
    private RequestForwardingResolver requestForwardingResolver;
    private HttpRequestMapper httpRequestMapper;
    private HttpClientProvider httpClientProvider;
    private HttpResponseMapper httpResponseMapper;

    public CharonProxyFilter(int order, List<RequestForwardingConfiguration> requestForwardingConfigurations, HttpClientFactory httpClientFactory) {
        this.order = order;
        requestForwardingResolver = new RequestForwardingResolver(requestForwardingConfigurations);
        httpRequestMapper = new HttpRequestMapper();
        httpClientProvider = new HttpClientProvider(httpClientFactory);
        httpResponseMapper = new HttpResponseMapper();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        RequestForwardingConfiguration configuration = requestForwardingResolver.resolveRequestForwarding(request);
        if (configuration == null) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpRequest httpRequest = httpRequestMapper.map(request);
        RequestForwarding forwarding = new RequestForwarding(configuration.getName(), configuration.getTimeoutConfiguration(), configuration.getCustomConfiguration());
        RequestForwarder forwarder = new RequestForwarder(configuration.getRequestForwardingInterceptors(), httpClientProvider);
        HttpResponse httpResponse = forwarder.forward(httpRequest, forwarding);
        httpResponseMapper.map(httpResponse, response);
    }
}
