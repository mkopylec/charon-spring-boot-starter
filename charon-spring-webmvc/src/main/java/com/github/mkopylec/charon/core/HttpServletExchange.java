package com.github.mkopylec.charon.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class HttpServletExchange {

    private HttpServletRequest request;
    private HttpServletResponse response;

    HttpServletExchange(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
