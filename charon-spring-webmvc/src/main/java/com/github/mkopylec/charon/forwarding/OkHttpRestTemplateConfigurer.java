package com.github.mkopylec.charon.forwarding;

public class OkHttpRestTemplateConfigurer extends RestTemplateConfigurer<OkHttpRestTemplateConfigurer> {

    private OkHttpRestTemplateConfigurer() {
        super(new RestTemplateConfiguration(new OkHttpRequestFactoryCreator()));
    }

    public static OkHttpRestTemplateConfigurer okHttpRestTemplate() {
        return new OkHttpRestTemplateConfigurer();
    }
}
