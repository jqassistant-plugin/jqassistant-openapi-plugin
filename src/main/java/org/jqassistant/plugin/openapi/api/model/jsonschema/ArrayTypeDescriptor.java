package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Array")
public interface ArrayTypeDescriptor extends TypeDescriptor {

    String TYPE_NAME = "array";

    @Relation("HAS_ITEMS")
    TypeDescriptor getItem();

    void setItem(TypeDescriptor item);
}