package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import io.swagger.v3.oas.models.headers.Header;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;

import java.util.List;

@Label("Header")
public interface HeaderDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    boolean getIsRequired();
    void setIsRequired(boolean isRequired);

    boolean getIsDeprecated();
    void setIsDeprecated(boolean isDeprecated);

    Header.StyleEnum getStyle();
    void setStyle(Header.StyleEnum style);

    boolean getExplode();
    void setExplode(boolean explode);

    Object getExample();
    void setExample(Object example);

    @Relation("DEFINES")
    SchemaDescriptor getSchema();
    void setSchema(SchemaDescriptor schema);

    @Relation("PROVIDES")
    List<ExampleDescriptor> getExamples();

    @Relation("CONTAINS")
    List<MediaTypeObjectDescriptor> getContent();
}
