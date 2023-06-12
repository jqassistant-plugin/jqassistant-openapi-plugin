package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("PathItems")
public interface PathItemDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
}
