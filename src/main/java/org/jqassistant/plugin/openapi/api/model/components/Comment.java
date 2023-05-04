package org.jqassistant.plugin.openapi.api.model.components;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Comment")
public interface Comment {
    String getContent();
    void setContent(String content);
}
