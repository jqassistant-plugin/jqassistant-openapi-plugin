package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Header")
public interface HeaderDescriptor extends OpenApiDescriptor{
    //TODO Implement Headers properly
    String getDescription();
    void setDescription(String description);
}
