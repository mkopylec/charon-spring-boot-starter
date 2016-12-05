package com.github.mkopylec.charon.core.hystrix;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

import static com.github.mkopylec.charon.configuration.HystrixProperties.HYSTRIX_GROUP_KEY;
import static com.github.mkopylec.charon.configuration.HystrixProperties.HYSTRIX_THREAD_POOL_KEY;
import static com.netflix.hystrix.HystrixCommand.Setter.withGroupKey;

public class CommandCreator {

    protected final CharonProperties charon;
    protected final HystrixThreadPoolProperties.Setter threadPoolConfiguration;

    public CommandCreator(CharonProperties charon) {
        this.charon = charon;
        threadPoolConfiguration = configureThreadPool();
    }

    public HystrixCommand<ResponseEntity<byte[]>> createHystrixCommand(String name, Supplier<ResponseEntity<byte[]>> requestSender) {
        Setter configuration = withGroupKey(HystrixCommandGroupKey.Factory.asKey(HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(name))
                .andCommandPropertiesDefaults(configureCommand())
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(HYSTRIX_THREAD_POOL_KEY))
                .andThreadPoolPropertiesDefaults(threadPoolConfiguration);
        return new HystrixCommand<ResponseEntity<byte[]>>(configuration) {

            @Override
            protected ResponseEntity<byte[]> run() throws Exception {
                return requestSender.get();
            }
        };
    }

    protected HystrixThreadPoolProperties.Setter configureThreadPool() {
        return HystrixThreadPoolProperties.defaultSetter()
                .withCoreSize(charon.getHystrix().getThreadPool().getCoreSize())
                .withMaxQueueSize(charon.getHystrix().getThreadPool().getMaximumQueueSize());
    }

    protected HystrixCommandProperties.Setter configureCommand() {
        return HystrixCommandProperties.defaultSetter()
                .withCircuitBreakerEnabled(true)
                .withExecutionTimeoutEnabled(false);
    }
}
