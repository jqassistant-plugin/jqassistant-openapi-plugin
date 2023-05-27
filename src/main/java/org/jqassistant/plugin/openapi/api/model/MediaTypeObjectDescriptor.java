package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("MediaTypeObject")
public interface MediaTypeObjectDescriptor extends OpenApiDescriptor{
    // TODO (TBD) implement examples property
    // TODO (TBD) implement encoding property
    // TODO (TBD) implement SchemaDescriptor

    String getMediaType();
    void setMediaType(String mediaType);
}
