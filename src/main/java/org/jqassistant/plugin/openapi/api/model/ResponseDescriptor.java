package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Response")
public interface ResponseDescriptor extends OpenApiDescriptor{
    Boolean getIsDefault();
    void setIsDefault(Boolean isDefault);
    String getStatusCode();
    void setStatusCode(String statusCode);
    String getDescription();
    void setDescription(String description);

    @Relation("CONTAINS")
    List<MediaTypeObjectDescriptor> getMediaTypeObject();
}
