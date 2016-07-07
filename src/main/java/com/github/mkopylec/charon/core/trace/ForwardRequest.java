package com.github.mkopylec.charon.core.trace;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class ForwardRequest extends IncomingRequest {

    protected String mappingName;

    public String getMappingName() {
        return trimToEmpty(mappingName);
    }

    @SuppressWarnings("unchecked")
    public <T extends ForwardRequest> T setMappingName(String mappingName) {
        this.mappingName = mappingName;
        return (T) this;
    }
}
