package com.github.mkopylec.charon;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.configuration.CharonConfiguration;

import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

public class CharonProxyFilter extends OncePerRequestFilter implements Ordered {

    private CharonConfiguration charonConfiguration;

    public CharonProxyFilter(CharonConfiguration charonConfiguration) {
        this.charonConfiguration = charonConfiguration;
    }

    @Override
    public int getOrder() {
        return charonConfiguration.getFilterOrder();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        // TODO
        // Choose request forwarding
        // Create HttpRequest
        // Apply all interceptors
        // Forward HttpRequest
        // Map incoming response to HttpResponse
        // Map HttpResponse to HttpServletResponse
    }
}
