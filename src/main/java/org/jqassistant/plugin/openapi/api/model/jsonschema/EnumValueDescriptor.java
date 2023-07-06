package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("EnumValue")
public interface EnumValueDescriptor extends JsonSchemaDescriptor {
    String getEnumName();
    void setEnumName(String enumName);

    int getEnumNumber();
    void setEnumNumber(int enumNumber);
}