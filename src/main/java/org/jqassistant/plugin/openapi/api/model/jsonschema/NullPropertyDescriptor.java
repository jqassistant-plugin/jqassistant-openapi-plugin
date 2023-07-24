package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Null")
public interface NullPropertyDescriptor extends PropertyDescriptor {

    String TYPE_NAME = "null";

}