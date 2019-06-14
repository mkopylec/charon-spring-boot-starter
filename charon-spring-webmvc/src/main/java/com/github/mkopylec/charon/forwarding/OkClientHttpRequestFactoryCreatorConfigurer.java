package com.github.mkopylec.charon.forwarding;

import okhttp3.OkHttpClient.Builder;

public class OkClientHttpRequestFactoryCreatorConfigurer extends ClientHttpRequestFactoryCreatorConfigurer<OkClientHttpRequestFactoryCreator> {

    private OkClientHttpRequestFactoryCreatorConfigurer() {
        super(new OkClientHttpRequestFactoryCreator());
    }

    public static OkClientHttpRequestFactoryCreatorConfigurer okClientHttpRequestFactoryCreator() {
        return new OkClientHttpRequestFactoryCreatorConfigurer();
    }

    public OkClientHttpRequestFactoryCreatorConfigurer httpClient(Builder httpClient) {
        configuredObject.setHttpClient(httpClient.build());
        return this;
    }
}
