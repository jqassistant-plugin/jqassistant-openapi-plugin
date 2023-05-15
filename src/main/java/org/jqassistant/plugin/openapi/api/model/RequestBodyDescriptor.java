package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("RequestBody")
public interface RequestBodyDescriptor {

    String getDescription();
    void setDescription(String description);

    Boolean getIsRequired();
    void setIsRequired(Boolean isRequired);

    @Relation("CONTAINS")
    MediaTypeObjectDescriptor getMediaTypeObject();
}
