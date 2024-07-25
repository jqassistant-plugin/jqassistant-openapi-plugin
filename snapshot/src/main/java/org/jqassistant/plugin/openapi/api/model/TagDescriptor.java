package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

/**
 * Represents a Tag of an OpenAPI Contract
 */
@Label("Tag")
public interface TagDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);

    @Relation("REFERENCES_EXTERNAL_DOCS")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);
}
