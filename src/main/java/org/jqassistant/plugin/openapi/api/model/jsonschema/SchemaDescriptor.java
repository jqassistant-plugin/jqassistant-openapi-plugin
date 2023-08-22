package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.plugin.openapi.api.model.ExternalDocsDescriptor;

import java.util.List;

@Label("Schema")
public interface SchemaDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);

    @Relation("REFERENCES")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDoc);

    @Relation("IS")
    TypeDescriptor getIsType();
    void setIsType(TypeDescriptor typeDescriptor);

    @Relation("DISCRIMINATOR")
    DiscriminatorDescriptor getDiscriminator();
    void setDiscriminator(DiscriminatorDescriptor discriminator);

    @Relation("ALL_OF")
    List<TypeDescriptor> getAllOfSchemas();

    @Relation("ONE_OF")
    List<TypeDescriptor> getOneOfSchemas();

    @Relation("ANY_OF")
    List<TypeDescriptor> getAnyOfSchemas();
}
