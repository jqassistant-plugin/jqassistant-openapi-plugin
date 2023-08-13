package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("RequestBody")
public interface RequestBodyDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    @Relation("CONTAINS")
    List<MediaTypeObjectDescriptor> getMediaTypeObjects();

    Boolean getIsRequired();
    void setIsRequired(Boolean isRequired);
}
