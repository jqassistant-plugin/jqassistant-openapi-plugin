package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Extension")
public interface ExtensionDescriptor extends OpenApiDescriptor {

    String getKey();
    void setKey(String key);

    Object getValue();
    void setValue(Object value);
}
