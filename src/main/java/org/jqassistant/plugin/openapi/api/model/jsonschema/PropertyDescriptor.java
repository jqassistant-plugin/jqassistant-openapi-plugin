package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Property")
public interface PropertyDescriptor extends JsonSchemaDescriptor {

    String getName();
    void setName(String name);

    @Relation("IS_TYPE")
    TypeDescriptor getType();
    void setType(TypeDescriptor type);
}
