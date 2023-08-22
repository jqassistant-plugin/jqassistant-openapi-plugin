package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Number")
public interface NumberTypeDescriptor extends TypeDescriptor {

    String TYPE_NAME = "number";
}