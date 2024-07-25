package org.jqassistant.plugin.openapi.api.model;


import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;

import java.util.List;

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

    Parameter.StyleEnum getStyle();
    void setStyle(Parameter.StyleEnum style);

    boolean getExplode();
    void setExplode(boolean explode);

    boolean getAllowsReserved();
    void setAllowsReserved(boolean allowsReserved);

    @Relation("DEFINED_BY")
    SchemaDescriptor getSchema();
    void setSchema(SchemaDescriptor schema);

    Object getExample();
    void setExample(Object example);

    @Relation("PROVIDES_EXAMPLE")
    List<ExampleDescriptor> getExamples();

    @Relation("CONTAINS")
    List<MediaTypeDescriptor> getContent();
}
