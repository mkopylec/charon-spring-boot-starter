package com.github.mkopylec.charon.forwarding;

public class OkClientHttpRequestFactoryCreatorConfigurer extends ClientHttpRequestFactoryCreatorConfigurer<OkClientHttpRequestFactoryCreator> {

    private OkClientHttpRequestFactoryCreatorConfigurer() {
        super(new OkClientHttpRequestFactoryCreator());
    }

    public static OkClientHttpRequestFactoryCreatorConfigurer okClientHttpRequestFactoryCreator() {
        return new OkClientHttpRequestFactoryCreatorConfigurer();
    }
}
