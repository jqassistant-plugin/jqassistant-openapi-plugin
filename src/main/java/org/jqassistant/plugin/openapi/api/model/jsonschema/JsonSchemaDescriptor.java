package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Abstract
@Label("JsonSchema")
public interface JsonSchemaDescriptor extends Descriptor {
}