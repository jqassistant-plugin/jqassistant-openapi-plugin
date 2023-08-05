package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Discriminator")
public interface DiscriminatorDescriptor extends JsonSchemaDescriptor {
    PropertyDescriptor getProperty();
    void setProperty(PropertyDescriptor property);

}