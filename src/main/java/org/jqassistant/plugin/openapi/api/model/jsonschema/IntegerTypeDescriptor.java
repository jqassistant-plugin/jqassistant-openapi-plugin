package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Integer")
public interface IntegerTypeDescriptor extends TypeDescriptor {

    String TYPE_NAME = "integer";
}