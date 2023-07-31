package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("External Documentation")
public interface ExternalDocsDescriptor extends OpenApiDescriptor{

    String getDescription();
    void setDescription(String description);

    String getUrl();
    void setUrl(String url);
}
