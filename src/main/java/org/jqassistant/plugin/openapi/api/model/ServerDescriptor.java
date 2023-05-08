package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Represents a Server of an OpenAPI Contract
 */
@Label("Server")
public interface ServerDescriptor extends OpenApiDescriptor {
    String getUrl();
    void setUrl(String url);
    String getDescription();
    void setDescription(String description);

    // TBD
    // Parameter / Variables etc
}
