package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Schema")
public interface SchemaDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);

    @Relation("HAS_PROPERTY")
    PropertyDescriptor getProperty();
}
