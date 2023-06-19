package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Schema")
public interface SchemaDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);

    @Relation("HAS_PROPERTY")
    List<PropertyDescriptor> getProperties();

    @Relation("ALL_OF")
    DiscriminatorDescriptor getDiscriminatorAllOf();
    void setDiscriminatorAllOf(DiscriminatorDescriptor discriminator);

    @Relation("ONE_OF")
    DiscriminatorDescriptor getDiscriminatorOneOf();
    void setDiscriminatorOneOf(DiscriminatorDescriptor discriminator);

    @Relation("ANY_OF")
    DiscriminatorDescriptor getDiscriminatorAnyOf();
    void setDiscriminatorAnyOf(DiscriminatorDescriptor discriminator);
}
