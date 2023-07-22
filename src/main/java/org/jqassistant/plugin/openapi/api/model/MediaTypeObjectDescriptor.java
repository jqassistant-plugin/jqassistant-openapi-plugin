package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("MediaTypeObject")
public interface MediaTypeObjectDescriptor extends OpenApiDescriptor{
    String getMediaType();
    void setMediaType(String mediaType);
}
