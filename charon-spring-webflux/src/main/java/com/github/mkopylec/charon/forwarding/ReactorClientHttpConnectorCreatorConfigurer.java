package com.github.mkopylec.charon.forwarding;

public class ReactorClientHttpConnectorCreatorConfigurer extends ClientHttpConnectorCreatorConfigurer<ReactorClientHttpConnectorCreator> {

    private ReactorClientHttpConnectorCreatorConfigurer() {
        super(new ReactorClientHttpConnectorCreator());
    }

    public static ReactorClientHttpConnectorCreatorConfigurer reactorClientHttpConnectorCreator() {
        return new ReactorClientHttpConnectorCreatorConfigurer();
    }
}
