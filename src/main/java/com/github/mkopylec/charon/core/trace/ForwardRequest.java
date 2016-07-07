package com.github.mkopylec.charon.core.trace;

public class ForwardRequest extends IncomingRequest {

    protected String mappingName;

    public String getMappingName() {
        return mappingName;
    }

    @SuppressWarnings("unchecked")
    public <T extends ForwardRequest> T setMappingName(String mappingName) {
        this.mappingName = mappingName;
        return (T) this;
    }
}
