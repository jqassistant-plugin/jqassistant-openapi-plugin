package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Enum")
public interface EnumStringTypeDescriptor extends StringTypeDescriptor {
    @Relation("HAS_VALUES")
    List<EnumValueDescriptor> getValues();

    void setValues(List<EnumValueDescriptor> values);
}