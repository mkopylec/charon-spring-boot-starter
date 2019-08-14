package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import org.springframework.core.Ordered;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ReverseProxyFilter extends OncePerRequestFilter implements Ordered {

    private int order;
    private RequestMappingResolver requestMappingResolver;
    private HttpRequestMapper httpRequestMapper;
    private RestTemplateProvider restTemplateProvider;
    private HttpResponseMapper httpResponseMapper;

    public ReverseProxyFilter(int order, RequestMappingProvider requestMappingProvider) {
        this.order = order;
        requestMappingResolver = new RequestMappingResolver(requestMappingProvider);
        httpRequestMapper = new HttpRequestMapper();
        restTemplateProvider = new RestTemplateProvider();
        httpResponseMapper = new HttpResponseMapper();
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        RequestMappingConfiguration configuration = requestMappingResolver.resolveRequestMapping(request);
        if (configuration == null) {
            filterChain.doFilter(request, response);
            return;
        }
        RequestEntity<byte[]> requestEntity = httpRequestMapper.map(request);
        RestTemplate restTemplate = restTemplateProvider.getRestTemplate(configuration);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(requestEntity, byte[].class);
        httpResponseMapper.map(responseEntity, response);
    }
}
