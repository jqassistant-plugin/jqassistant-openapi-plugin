package org.jqassistant.plugin.openapi.impl.jsonschema;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Schema;
import org.jqassistant.plugin.openapi.api.model.jsonschema.*;
import org.jqassistant.plugin.openapi.impl.parsers.ExternalDocsParser;
import org.jqassistant.plugin.openapi.impl.util.Resolver;
import org.jqassistant.plugin.openapi.impl.util.UnknownTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonSchemaParser {

    private final Resolver resolver;
    private final Store store;
    private static final String SCHEMA_REFSTRING = "#/components/schemas/";

    public JsonSchemaParser(Resolver resolver, Store store) {
        this.resolver = resolver;
        this.store = store;
    }

    public List<SchemaDescriptor> parseAllSchemas(Map<String, Schema> schemasMap){
        return schemasMap.entrySet().stream().map(schemaEntry -> parseSchema(schemaEntry.getValue(), schemaEntry.getKey())).collect(Collectors.toList());
    }

    public SchemaDescriptor parseSchema(Schema<?> schema, String name){

        SchemaDescriptor schemaDescriptor = resolver.resolve(SCHEMA_REFSTRING + name);

        if(schema.getExternalDocs() != null)
            schemaDescriptor.setExternalDocs(ExternalDocsParser.parseOne(schema.getExternalDocs(), store));

        schemaDescriptor.setObject(parseProperty(schema, name));

        return schemaDescriptor;

    }

    private PropertyDescriptor parseProperty(Schema<?> property, String name){
        PropertyDescriptor propertyDescriptor;

        if (property == null)
            return null; // No property present

        if (property.getType() == null) {
            if (property.get$ref() != null)
                return parseReference(property.get$ref());
            else
                return null; // No property present
        }

        switch (property.getType()) {
            case ArrayPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseArray(property);
                break;
            case BoolPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseBoolean(property);
                break;
            case IntegerPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseInteger(property);
                break;
            case NullPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseNull(property);
                break;
            case NumberPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseNumber(property);
                break;
            case ObjectPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseObject(name, property);
                break;
            case StringPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseString(property);
                break;
            default:
                throw new UnknownTypeException("Unknown Type \"" + property.getType() + "\" in Schema: " + name);
        }


        if (name != null)
            propertyDescriptor.setName(name);

        return propertyDescriptor;
    }

    public ObjectPropertyDescriptor parseObject(final String name, final Schema<?> schema){

        ObjectPropertyDescriptor objectPropertyDescriptor = store.create(ObjectPropertyDescriptor.class);
        objectPropertyDescriptor.setName(name);

        for (String key : schema.getProperties().keySet()) {
            PropertyDescriptor prop = parseProperty(schema.getProperties().get(key), key);
            if (prop == null)
                continue;
            objectPropertyDescriptor.getProperties().add(prop);
        }

        return objectPropertyDescriptor;

    }

    ArrayPropertyDescriptor parseArray(final Schema<?> schema){

        ArrayPropertyDescriptor arrayPropertyDescriptor = parseDescription(schema, store.create(ArrayPropertyDescriptor.class));
        arrayPropertyDescriptor.setItem(parseProperty(schema.getItems(), "items"));

        return arrayPropertyDescriptor;
    }

    BoolPropertyDescriptor parseBoolean(final Schema<?> schema){

        return parseDescription(schema, store.create(BoolPropertyDescriptor.class));
    }

    EnumStringPropertyDescriptor parseEnum(final Schema<?> schema){

        EnumStringPropertyDescriptor enumStringPropertyDescriptor = store.create(EnumStringPropertyDescriptor.class);
        enumStringPropertyDescriptor.setValues(parseEnumValues(schema.getEnum()));

        return enumStringPropertyDescriptor;
    }

    List<EnumValueDescriptor> parseEnumValues(List<?> enums){
        List<EnumValueDescriptor> ret = new ArrayList<>();

        enums.forEach( val -> {
            EnumValueDescriptor enumValueDescriptor = store.create(EnumValueDescriptor.class);
            enumValueDescriptor.setEnumName(val.toString());
            ret.add(enumValueDescriptor);
        });

        return ret;
    }

    IntegerPropertyDescriptor parseInteger(final Schema<?> schema){

        return parseDescriptionAndFormat(schema, store.create(IntegerPropertyDescriptor.class));
    }

    NullPropertyDescriptor parseNull(final Schema<?> schema){

        return parseDescription(schema, store.create(NullPropertyDescriptor.class));
    }

    NumberPropertyDescriptor parseNumber(final Schema<?> schema){

        return parseDescriptionAndFormat(schema, store.create(NumberPropertyDescriptor.class));
    }

    ReferencePropertyDescriptor parseReference(String ref){
        ReferencePropertyDescriptor referencePropertyDescriptor = store.create(ReferencePropertyDescriptor.class);

        referencePropertyDescriptor.setReference(resolver.resolve(ref));

        return referencePropertyDescriptor;
    }

    StringPropertyDescriptor parseString(final Schema<?> schema) {

        if (schema.getEnum() == null || schema.getEnum().isEmpty()) {
            return parseDescriptionAndFormat(schema, store.create(StringPropertyDescriptor.class));
        } else {
            EnumStringPropertyDescriptor enumStringPropertyDescriptor = parseEnum(schema);
            return parseDescription(schema, enumStringPropertyDescriptor);
        }
    }

    <D extends PropertyDescriptor> D parseDescription(Schema<?> schema, D propertyDescriptor){

        if (schema.getDescription() != null)
            propertyDescriptor.setDescription(schema.getDescription());

        return propertyDescriptor;
    }

    <D extends PropertyDescriptor> D parseDescriptionAndFormat(Schema<?> schema, D propertyDescriptor) {

        if (schema.getFormat() != null)
            propertyDescriptor.setFormat(schema.getFormat());

        return propertyDescriptor;
    }

}
