package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.configuration.Valid;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.springframework.util.Assert.isTrue;

// TODO use ThreadPoolExecutor instead to be compatible with Reactor's subscribeOn(scheduler)
class ThreadPool extends ThreadPoolTaskExecutor implements Valid {

    private boolean initialized;

    ThreadPool() {
    }

    @Override
    public void validate() {
        isTrue(getCorePoolSize() >= 0, "Invalid thread pool initial size: " + getCorePoolSize());
        isTrue(getMaxPoolSize() > 0, "Invalid thread pool maximum size: " + getMaxPoolSize());
        isTrue(getCorePoolSize() <= getMaxPoolSize(), "Thread pool initial size: " + getCorePoolSize() + " is greater than maximum size: " + getMaxPoolSize());
    }

    @Override
    public void initialize() {
        if (!initialized) {
            super.initialize();
            initialized = true;
        }
    }
}
