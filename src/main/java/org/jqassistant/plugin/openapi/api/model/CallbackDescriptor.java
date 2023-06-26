package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Callback")
public interface CallbackDescriptor extends OpenApiDescriptor{
    String getRef();
    void setRef(String ref);
}
