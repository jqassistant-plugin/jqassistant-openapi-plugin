package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Example")
public interface ExampleDescriptor extends OpenApiDescriptor{

    String getDescription();
    void setDescription(String description);
}
