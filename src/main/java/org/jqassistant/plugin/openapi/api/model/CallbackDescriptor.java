package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Callbacks")
public interface CallbackDescriptor extends OpenApiDescriptor{
    //TODO Implement Callback properly
    String set$ref();
    void set$ref(String $ref);
}
