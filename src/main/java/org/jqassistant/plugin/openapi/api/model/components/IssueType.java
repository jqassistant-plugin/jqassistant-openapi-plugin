package org.jqassistant.plugin.openapi.api.model.components;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Issue Type")
public interface IssueType {
    String getTypeName();
    void setTypeName(String typeName);
}
