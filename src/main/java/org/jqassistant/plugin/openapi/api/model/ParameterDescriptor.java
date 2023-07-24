package org.jqassistant.plugin.openapi.api.model;


import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Parameter")
public interface ParameterDescriptor extends OpenApiDescriptor{
    enum ParameterLocation {
        QUERY, HEADER, PATH, COOKIE
    }

    String getName();
    void setName(String name);
    ParameterLocation getLocation();
    void setLocation(ParameterLocation location);
    String getDescription();
    void setDescription(String description);
    Boolean getIsRequired();
    void setIsRequired(Boolean isRequired);
    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);
    Boolean getAllowsEmptyValue();
    void setAllowsEmptyValue(Boolean allowsEmptyValue);
}
