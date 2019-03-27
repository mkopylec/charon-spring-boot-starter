package com.github.mkopylec.charon.forwarding;

public class OkHttpClientFactoryConfigurer extends HttpClientFactoryConfigurer<OkHttpClientFactory> {

    private OkHttpClientFactoryConfigurer() {
        super(new OkHttpClientFactory());
    }

    public static OkHttpClientFactoryConfigurer okHttpClientFactory() {
        return new OkHttpClientFactoryConfigurer();
    }
}
