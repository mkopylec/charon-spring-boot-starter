package com.github.mkopylec.charon.configuration;

import java.time.Duration;
import java.util.function.Supplier;

public class DynamicCharonConfigurer extends Configurer<DynamicCharonConfiguration> {

    private DynamicCharonConfigurer() {
        super(new DynamicCharonConfiguration());
    }

    public static DynamicCharonConfigurer dynamicCharon() {
        return new DynamicCharonConfigurer();
    }

    public DynamicCharonConfigurer update(Supplier<CharonConfigurer> update) {
        configuredObject.setUpdate(() -> update.get().configure());
        return this;
    }

    public DynamicCharonConfigurer updateRate(Duration updateRate) {
        configuredObject.setUpdateRate(updateRate);
        return this;
    }
}
