package com.github.mkopylec.charon.forwarding.interceptors.async;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import com.github.mkopylec.charon.configuration.Valid;

import static java.util.concurrent.TimeUnit.SECONDS;

class ThreadPool extends ThreadPoolExecutor implements Valid {

    ThreadPool() {
        super(3, 20, 60, SECONDS, new LinkedBlockingQueue<>(50));
    }
}
