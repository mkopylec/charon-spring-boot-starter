package com.github.mkopylec.charon.configuration;

import java.util.HashMap;
import java.util.Map;

import com.github.mkopylec.charon.utils.Valid;

public class CustomConfiguration implements Valid {

    private Map<String, Object> properties;

    CustomConfiguration() {
        properties = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <P> P getProperty(String name) {
        return (P) properties.get(name);
    }

    void setProperty(String name, Object value) {
        properties.put(name, value);
    }
}
