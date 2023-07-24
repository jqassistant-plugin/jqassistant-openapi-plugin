package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Discriminator")
public interface DiscriminatorDescriptor extends JsonSchemaDescriptor {
    String getPropertyName();
    void setPropertyName(String propertyName);

    @Relation("REF")
    List<SchemaDescriptor> getSchemas();
}