package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("RequestBody")
public interface RequestBodyDescriptor extends OpenApiDescriptor{
    // TODO implement $ref
    // TODO implement extensions

    String getDescription();
    void setDescription(String description);
    Boolean getIsRequired();
    void setIsRequired(Boolean isRequired);

    @Relation("CONTAINS")
    List<MediaTypeObjectDescriptor> getMediaTypeObjects();
}
