package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Represents a Tag of an OpenAPI Contract
 */
@Label("Tag")
public interface TagDescriptor extends OpenApiDescriptor{
    String getTag();
    void setTag(String tag);
    String getDescription();
    void setDescription(String description);
}
