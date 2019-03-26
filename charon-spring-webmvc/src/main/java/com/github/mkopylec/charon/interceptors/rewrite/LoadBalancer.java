package com.github.mkopylec.charon.interceptors.rewrite;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.utils.Valid;

public interface LoadBalancer extends Valid {

    URI chooseServer(List<URI> servers);
}
