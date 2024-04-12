package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.plugin.openapi.api.model.ExternalDocsDescriptor;

/**
 * Represents a Tag of an OpenAPI Contract
 */
@Label("Tag")
public interface TagDescriptor extends AsyncApiDescriptor {
    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    String getRef();
    void setRef(String ref);

    @Relation("REFERENCES_EXTERNAL_DOCS")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);

}
