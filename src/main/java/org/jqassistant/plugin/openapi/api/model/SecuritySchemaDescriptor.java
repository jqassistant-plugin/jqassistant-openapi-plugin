package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("SecuritySchemas")
public interface SecuritySchemaDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
}
