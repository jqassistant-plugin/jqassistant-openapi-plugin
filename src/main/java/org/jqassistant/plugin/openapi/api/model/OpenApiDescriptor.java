package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

/**
 * Base label for OpenAPI related nodes.
 */
@Label("OpenAPI")
@Abstract
public interface OpenApiDescriptor extends Descriptor {
}
