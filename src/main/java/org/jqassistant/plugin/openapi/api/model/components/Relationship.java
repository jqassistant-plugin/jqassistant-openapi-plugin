package org.jqassistant.plugin.openapi.api.model.components;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Relationship")
public interface Relationship {
    Entity getStartNode();
    void setStartNode(Entity startNode);

    Entity getEndNode();
    void setEndNode(Entity endNode);

    // Other methods specific to the relationship
}
