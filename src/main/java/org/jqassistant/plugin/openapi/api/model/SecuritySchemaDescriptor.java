package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("SecuritySchema")
public interface SecuritySchemaDescriptor extends OpenApiDescriptor{
    //TODO Implement SecuritySchema properly
    String getName();
    void setName(String name);
}
