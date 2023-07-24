package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Object")
public interface ObjectPropertyDescriptor extends PropertyDescriptor {

    String TYPE_NAME = "object";

    @Relation("HAS_PROPERTY")
    List<PropertyDescriptor> getProperties();

}