package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Paths")
public interface PathsDescriptor extends OpenApiDescriptor{

    @Relation("DEFINES_PATH_ITEM")
    List<PathItemDescriptor> getPathItems();
}
