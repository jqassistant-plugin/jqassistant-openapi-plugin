package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("BOOL")
public interface BoolPropertyDescriptor extends PropertyDescriptor {
    boolean getBoolen();
    void setBoolean(boolean booleanValue);
}
