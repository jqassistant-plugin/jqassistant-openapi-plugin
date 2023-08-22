package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("DiscriminatorMapping")
public interface DiscriminatorMappingDescriptor extends JsonSchemaDescriptor {
    String getKey();
    void setKey(String value);

    String getValue();
    void setValue(String value);
}
