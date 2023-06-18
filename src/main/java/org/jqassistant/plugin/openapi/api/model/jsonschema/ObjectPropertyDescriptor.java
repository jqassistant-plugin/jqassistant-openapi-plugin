package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("OBJECT")
public interface ObjectPropertyDescriptor extends PropertyDescriptor {

    @Relation("HAS_PROPERTY")
    PropertyDescriptor getProperty();

    void setProperty(PropertyDescriptor property);
}
