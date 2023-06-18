package org.jqassistant.plugin.openapi.api.model.jsonschema;

public interface EnumStringPropertyDescriptor extends StringPropertyDescriptor {
    Enum getValues();

    void setEnum(Enum values);
}
