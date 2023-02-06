package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.configuration.Valid;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

class ThreadPool extends ThreadPoolExecutor implements Valid {

    ThreadPool() {
        super(3, 20, 60, SECONDS, new LinkedBlockingQueue<>(50));
    }
}
