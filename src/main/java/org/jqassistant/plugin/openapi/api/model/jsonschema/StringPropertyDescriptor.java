package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("String")
public interface StringPropertyDescriptor extends PropertyDescriptor {
    String TYPE_NAME = "string";
}