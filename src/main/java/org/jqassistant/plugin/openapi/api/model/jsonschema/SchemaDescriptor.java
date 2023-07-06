package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import org.jqassistant.plugin.openapi.api.model.OpenApiDescriptor;

@Label("Schema")
public interface SchemaDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);
}
