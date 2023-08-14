package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("RequestBody")
public interface RequestBodyDescriptor extends OpenApiDescriptor{
    String getDescription();
    void setDescription(String description);
    Boolean getIsRequired();
    void setIsRequired(Boolean isRequired);

    @Relation("CONTAINS")
    List<MediaTypeDescriptor> getMediaTypes();
}
