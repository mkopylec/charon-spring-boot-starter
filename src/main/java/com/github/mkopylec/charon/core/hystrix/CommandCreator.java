package com.github.mkopylec.charon.core.hystrix;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.configuration.MappingProperties.HystrixFallbackResponseProperties;
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
import static org.springframework.http.MediaType.valueOf;
import static org.springframework.http.ResponseEntity.status;

public class CommandCreator {

    protected final CharonProperties charon;
    protected final HystrixThreadPoolProperties.Setter threadPoolConfiguration;

    public CommandCreator(CharonProperties charon) {
        this.charon = charon;
        threadPoolConfiguration = configureThreadPool();
    }

    public HystrixCommand<ResponseEntity<byte[]>> createHystrixCommand(MappingProperties mapping, Supplier<ResponseEntity<byte[]>> requestSender) {
        Setter configuration = withGroupKey(HystrixCommandGroupKey.Factory.asKey(HYSTRIX_GROUP_KEY))
                .andCommandKey(HystrixCommandKey.Factory.asKey(mapping.getName()))
                .andCommandPropertiesDefaults(configureCommand())
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(HYSTRIX_THREAD_POOL_KEY))
                .andThreadPoolPropertiesDefaults(threadPoolConfiguration);
        return new HystrixCommand<ResponseEntity<byte[]>>(configuration) {

            @Override
            protected ResponseEntity<byte[]> run() throws Exception {
                return requestSender.get();
            }

            @Override
            protected ResponseEntity<byte[]> getFallback() {
                return createFallbackResponse(mapping);
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

    protected ResponseEntity<byte[]> createFallbackResponse(MappingProperties mapping) {
        HystrixFallbackResponseProperties hystrixFallbackResponse = mapping.getHystrixFallbackResponse();
        return status(hystrixFallbackResponse.getHttpStatus())
                .contentType(valueOf(hystrixFallbackResponse.getContentType()))
                .body(hystrixFallbackResponse.getBody().getBytes());
    }
}
