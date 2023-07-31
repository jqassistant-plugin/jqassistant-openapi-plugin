package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.plugin.openapi.api.model.ExternalDocsDescriptor;

@Label("Schema")
public interface SchemaDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);

    @Relation("REFERENCES")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDoc);

    @Relation("IS")
    PropertyDescriptor getObject();
    void setObject(PropertyDescriptor propertyDescriptor);

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
