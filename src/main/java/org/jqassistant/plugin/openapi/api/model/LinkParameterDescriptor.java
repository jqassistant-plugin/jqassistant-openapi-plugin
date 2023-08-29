package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("LinkParameter")
public interface LinkParameterDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getValue();
    void setValue(String value);
}
