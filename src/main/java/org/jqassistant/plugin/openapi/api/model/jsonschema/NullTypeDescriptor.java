package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Null")
public interface NullTypeDescriptor extends TypeDescriptor {

    String TYPE_NAME = "null";

}