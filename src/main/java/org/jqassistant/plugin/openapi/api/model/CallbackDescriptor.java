package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Callback")
public interface CallbackDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getRef();
    void setRef(String ref);

    @Relation("REFERENCES_PATH_ITEM")
    List<PathItemDescriptor> getPathItems();
}
