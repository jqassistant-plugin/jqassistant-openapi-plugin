package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;

import java.net.URL;

/**
 * Represents a Server of an OpenAPI Contract
 */
@Label("Server")
public interface ServerDescriptor extends OpenApiDescriptor, NamedDescriptor {
    URL getUrl();
    String getDescription();

    // TBD
    // Parameter / Variables etc
}
