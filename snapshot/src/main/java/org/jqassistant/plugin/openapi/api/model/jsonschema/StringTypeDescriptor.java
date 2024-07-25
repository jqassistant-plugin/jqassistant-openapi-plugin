package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("String")
public interface StringTypeDescriptor extends TypeDescriptor {
    String TYPE_NAME = "string";
}