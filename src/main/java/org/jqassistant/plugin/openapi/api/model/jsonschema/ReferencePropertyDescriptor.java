package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Reference")
public interface ReferencePropertyDescriptor extends PropertyDescriptor {

    @Relation("REF")
    SchemaDescriptor getReference();

    void setReference(SchemaDescriptor reference);
}