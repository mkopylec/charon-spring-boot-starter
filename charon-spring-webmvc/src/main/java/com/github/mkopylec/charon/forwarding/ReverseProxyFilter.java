package com.github.mkopylec.charon.forwarding;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;

import org.springframework.core.Ordered;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

public class ReverseProxyFilter extends OncePerRequestFilter implements Ordered {

    private int order;
    private RequestMappingResolver requestMappingResolver;
    private HttpRequestMapper httpRequestMapper;
    private RestTemplateProvider restTemplateProvider;
    private HttpResponseMapper httpResponseMapper;

    public ReverseProxyFilter(int order, List<RequestMappingConfiguration> requestMappingConfigurations) {
        this.order = order;
        requestMappingResolver = new RequestMappingResolver(requestMappingConfigurations);
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
