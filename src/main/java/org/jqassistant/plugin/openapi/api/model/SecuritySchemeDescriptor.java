package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("SecuritySchema")
public interface SecuritySchemeDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
}
