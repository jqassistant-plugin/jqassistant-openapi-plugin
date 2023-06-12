package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Examples")
public interface ExampleDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
}
