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
    private PropertyResolver propertyResolver;

    private final Store store;
    private static final String SCHEMA_REFSTRING = "#/components/schemas/";

    public JsonSchemaParser(Resolver resolver, Store store) {
        this.resolver = resolver;
        this.store = store;
    }

    private JsonSchemaParser(Resolver resolver, Store store, PropertyResolver propertyResolver) {
        this.resolver = resolver;
        this.store = store;
        this.propertyResolver = propertyResolver;
    }



    public List<SchemaDescriptor> parseAllSchemas(Map<String, Schema> schemasMap){
        return schemasMap.entrySet().stream().map((schemaEntry) -> {
            // Create new Parser instance with new PropertyResolver for scope of this schema only
            JsonSchemaParser jsonSchemaParser = new JsonSchemaParser(resolver, store, new PropertyResolver(store));
            return jsonSchemaParser.parseSchema(schemaEntry.getValue(), schemaEntry.getKey()); // Parse with new Parser instance
        }).collect(Collectors.toList());
    }

    public SchemaDescriptor parseSchema(Schema<?> schema, String name){

        SchemaDescriptor schemaDescriptor = resolver.resolve(SCHEMA_REFSTRING + name);

        if(schema.getExternalDocs() != null)
            schemaDescriptor.setExternalDocs(ExternalDocsParser.parseOne(schema.getExternalDocs(), store));

        if (schema.getAllOf() != null && !schema.getAllOf().isEmpty()){
            for (Schema x : schema.getAllOf()){
                schemaDescriptor.getAllOfSchemas().add(parseXof(x, name));
            }
        } else if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()){
            for (Schema x : schema.getAllOf()){
                schemaDescriptor.getOneOfSchemas().add(parseXof(x, name));
            }
        } else if (schema.getAnyOf() != null && !schema.getAnyOf().isEmpty()){
            for (Schema x : schema.getAllOf()){
                schemaDescriptor.getAnyOfSchemas().add(parseXof(x, name));
            }
        } else {
            schemaDescriptor.setObject(parseProperty(schema, name)); // The main parsing of the schema
        }

        if (schema.getDiscriminator() != null){
            DiscriminatorDescriptor discriminatorDescriptor = store.create(DiscriminatorDescriptor.class);

            if (schema.getDiscriminator().getPropertyName() != null)
                discriminatorDescriptor.setProperty(propertyResolver.resolve(schema.getDiscriminator().getPropertyName()));

            // TODO: MAPPING

            schemaDescriptor.setDiscriminator(discriminatorDescriptor);
        }

        return schemaDescriptor;

    }

    private PropertyDescriptor parseProperty(Schema<?> property, String name){
        return parseProperty(property, name, false);
    }

    private PropertyDescriptor parseProperty(Schema<?> property, String name, boolean doNotCache){
        PropertyDescriptor propertyDescriptor;

        if (property == null)
            return null; // No property present

        if (property.getType() == null) {
            if (property.get$ref() != null)
                return parseReference(name, property.get$ref(), doNotCache);
            else
                return null; // No property present
        }

        switch (property.getType()) {
            case ArrayPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseArray(name, property, doNotCache);
                break;
            case BoolPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseBoolean(name, property, doNotCache);
                break;
            case IntegerPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseInteger(name, property, doNotCache);
                break;
            case NullPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseNull(name, property, doNotCache);
                break;
            case NumberPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseNumber(name, property, doNotCache);
                break;
            case ObjectPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseObject(name, property, doNotCache);
                break;
            case StringPropertyDescriptor.TYPE_NAME:
                propertyDescriptor = parseString(name, property, doNotCache);
                break;
            default:
                throw new UnknownTypeException("Unknown Type \"" + property.getType() + "\" in Schema: " + name);
        }

        return propertyDescriptor;
    }

    public ObjectPropertyDescriptor parseObject(final String name, final Schema<?> schema, boolean doNotCache){

        ObjectPropertyDescriptor objectPropertyDescriptor = propertyResolver.resolve(name, ObjectPropertyDescriptor.class, doNotCache);

        for (String key : schema.getProperties().keySet()) {
            PropertyDescriptor prop = parseProperty(schema.getProperties().get(key), key);
            if (prop == null)
                continue;
            objectPropertyDescriptor.getProperties().add(prop);
        }

        return objectPropertyDescriptor;

    }

    ArrayPropertyDescriptor parseArray(final String name, final Schema<?> schema, boolean doNotCache){

        ArrayPropertyDescriptor arrayPropertyDescriptor = parseDescription(schema, propertyResolver.resolve(name, ArrayPropertyDescriptor.class, doNotCache));
        arrayPropertyDescriptor.setItem(parseProperty(schema.getItems(), "items", true));

        return arrayPropertyDescriptor;
    }

    BoolPropertyDescriptor parseBoolean(final String name, final Schema<?> schema, boolean doNotCache){

        return parseDescription(schema, propertyResolver.resolve(name, BoolPropertyDescriptor.class, doNotCache));
    }

    EnumStringPropertyDescriptor parseEnum(final String name, final Schema<?> schema, boolean doNotCache){

        EnumStringPropertyDescriptor enumStringPropertyDescriptor = propertyResolver.resolve(name, EnumStringPropertyDescriptor.class, doNotCache);
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

    IntegerPropertyDescriptor parseInteger(final String name, final Schema<?> schema, boolean doNotCache){

        return parseDescriptionAndFormat(schema, propertyResolver.resolve(name, IntegerPropertyDescriptor.class, doNotCache));
    }

    NullPropertyDescriptor parseNull(final String name, final Schema<?> schema, boolean doNotCache){

        return parseDescription(schema, propertyResolver.resolve(name, NullPropertyDescriptor.class, doNotCache));
    }

    NumberPropertyDescriptor parseNumber(final String name, final Schema<?> schema, boolean doNotCache){

        return parseDescriptionAndFormat(schema, propertyResolver.resolve(name, NumberPropertyDescriptor.class, doNotCache));
    }

    ReferencePropertyDescriptor parseReference(final String name, String ref, boolean doNotCache){

        ReferencePropertyDescriptor referencePropertyDescriptor = propertyResolver.resolve(name, ReferencePropertyDescriptor.class, doNotCache);

        referencePropertyDescriptor.setReference(resolver.resolve(ref));

        return referencePropertyDescriptor;
    }

    StringPropertyDescriptor parseString(final String name, final Schema<?> schema, boolean doNotCache) {

        if (schema.getEnum() == null || schema.getEnum().isEmpty()) {
            return parseDescriptionAndFormat(schema, propertyResolver.resolve(name, StringPropertyDescriptor.class, doNotCache));
        } else {
            EnumStringPropertyDescriptor enumStringPropertyDescriptor = parseEnum(name, schema, doNotCache);
            return parseDescription(schema, enumStringPropertyDescriptor);
        }
    }

    private PropertyDescriptor parseXof(Schema x, String name){
        if (x.get$ref() != null) {
            return parseReference(null, x.get$ref(), true);
        } else {
            return parseProperty(x, name, true);
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
