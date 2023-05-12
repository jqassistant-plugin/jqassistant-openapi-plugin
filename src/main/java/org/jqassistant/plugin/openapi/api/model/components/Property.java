package org.jqassistant.plugin.openapi.api.model.components;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Property")
public interface Property {
    String getKey();
    void setKey(String key);

    Object getValue();
    void setValue(Object value);
}

