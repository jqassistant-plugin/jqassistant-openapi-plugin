package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Links")
public interface LinkDescriptor extends OpenApiDescriptor{
    //TODO-imeplement LinkDescriptor properly
    String getOperationRef();
    void setOperationRef(String operationRef);
}
