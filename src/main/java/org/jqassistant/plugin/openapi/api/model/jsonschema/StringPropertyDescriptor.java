package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("STRING")
public interface StringPropertyDescriptor extends PropertyDescriptor {
    String getString();
    void setString(String string);
}
