package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("ServerVariable")
public interface ServerVariableDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String[] getPossibleValues();
    void setPossibleValues(String[] possibleValues);

    String getDefault();
    void setDefault(String defaultValue);

    String getDescription();
    void setDescription(String description);
}
