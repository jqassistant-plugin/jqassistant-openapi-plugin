package org.jqassistant.plugin.openapi.impl.jsonschema;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Discriminator;
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

    /**
     * Instantiate Parser to parse a Map of schemas
     *
     * @param resolver Resolves Schemas
     * @param store Creates new DB Objects
     */
    public JsonSchemaParser(Resolver resolver, Store store) {
        this.resolver = resolver;
        this.store = store;
    }

    /**
     * Additionally Instantiate a PropertyResolver with scope of only one Schema
     *
     * @param resolver Resolves Schemas
     * @param store Creates new DB Objects
     * @param propertyResolver Resolves Properties
     */
    private JsonSchemaParser(Resolver resolver, Store store, PropertyResolver propertyResolver) {
        this.resolver = resolver;
        this.store = store;
        this.propertyResolver = propertyResolver;
    }


    public List<SchemaDescriptor> parseAllSchemas(Map<String, Schema> schemasMap){
        return schemasMap.entrySet().stream().map(schemaEntry -> {
            // Create new Parser instance with new PropertyResolver for scope of this schema only
            JsonSchemaParser jsonSchemaParser = new JsonSchemaParser(resolver, store, new PropertyResolver(store));
            return jsonSchemaParser.parseSchema(schemaEntry.getValue(), schemaEntry.getKey()); // Parse with new Parser instance
        }).collect(Collectors.toList());
    }

    public SchemaDescriptor parseOneSchema(Schema<?> schema, final String name){
        JsonSchemaParser jsonSchemaParser = new JsonSchemaParser(resolver, store, new PropertyResolver(store));
        return jsonSchemaParser.parseSchema(schema, name);
    }

    private SchemaDescriptor parseSchema(Schema<?> schema, String name){

        SchemaDescriptor schemaDescriptor = resolver.resolve(SCHEMA_REFSTRING + name);

        if(schema.getExternalDocs() != null)
            schemaDescriptor.setExternalDocs(ExternalDocsParser.parseOne(schema.getExternalDocs(), store));

        if (schema.getAllOf() != null && !schema.getAllOf().isEmpty()){
            for (Schema<?> x : schema.getAllOf()){
                schemaDescriptor.getAllOfSchemas().add(parseObjectOrReference(x));
            }
        } else if (schema.getOneOf() != null && !schema.getOneOf().isEmpty()){
            for (Schema<?> x : schema.getOneOf()){
                schemaDescriptor.getOneOfSchemas().add(parseObjectOrReference(x));
            }
        } else if (schema.getAnyOf() != null && !schema.getAnyOf().isEmpty()){
            for (Schema<?> x : schema.getAnyOf()){
                schemaDescriptor.getAnyOfSchemas().add(parseObjectOrReference(x));
            }
        } else {
            schemaDescriptor.setIsType(parseType(schema)); // The main parsing of the schema
        }

        if (schema.getDiscriminator() != null){
            schemaDescriptor.setDiscriminator(parseDiscriminator(schema.getDiscriminator()));
        }

        return schemaDescriptor;

    }

    private DiscriminatorDescriptor parseDiscriminator(Discriminator discriminator) {
        DiscriminatorDescriptor discriminatorDescriptor = store.create(DiscriminatorDescriptor.class);

        if (discriminator.getPropertyName() != null)
            discriminatorDescriptor.setProperty(propertyResolver.resolve(discriminator.getPropertyName()));

        if (discriminator.getMapping() != null) {
            for (Map.Entry<String, String> entry : discriminator.getMapping().entrySet()) {
                DiscriminatorMappingDescriptor mappingDescriptor = store.create(DiscriminatorMappingDescriptor.class);
                mappingDescriptor.setKey(entry.getKey());
                mappingDescriptor.setValue(entry.getValue());
                discriminatorDescriptor.getMapping().add(mappingDescriptor);
            }
        }

        return discriminatorDescriptor;

    }

    private TypeDescriptor parseType(Schema<?> property){
        TypeDescriptor typeDescriptor;

        if (property == null)
            return null; // No Type present

        if (property.getType() == null) {
            if (property.get$ref() != null)
                return parseReference(property.get$ref());
            else
                return parseNull(property); // No Type present
        }

        switch (property.getType()) {
            case ArrayTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseArray(property);
                break;
            case BoolTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseBoolean(property);
                break;
            case IntegerTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseInteger(property);
                break;
            case NullTypeDescriptor.TYPE_NAME: // Fallback to old version if nulltype is declared
                typeDescriptor = parseNull(property);
                break;
            case NumberTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseNumber(property);
                break;
            case ObjectTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseObject(property);
                break;
            case StringTypeDescriptor.TYPE_NAME:
                typeDescriptor = parseString(property);
                break;
            default:
                throw new UnknownTypeException("Unknown Type \"" + property.getType() + "\"");
        }

        return typeDescriptor;
    }

    private ObjectTypeDescriptor parseObject(final Schema<?> schema){

        ObjectTypeDescriptor objectTypeDescriptor = store.create(
                ObjectTypeDescriptor.class);

        if (schema.getProperties() == null)
            return objectTypeDescriptor;

        for (String key : schema.getProperties().keySet()) {
            PropertyDescriptor prop = parseProperty(schema.getProperties().get(key), key);
            objectTypeDescriptor.getProperties().add(prop);
        }

        return objectTypeDescriptor;
    }

    private PropertyDescriptor parseProperty(final Schema<?> schema, final String name){

        PropertyDescriptor propertyDescriptor = propertyResolver.resolve(name);
        propertyDescriptor.setType(parseType(schema));

        if (schema.getDescription() != null)
            propertyDescriptor.setDescription(schema.getDescription());

        return propertyDescriptor;
    }

    private ArrayTypeDescriptor parseArray(final Schema<?> schema){

        ArrayTypeDescriptor arrayTypeDescriptor = store.create(ArrayTypeDescriptor.class);
        arrayTypeDescriptor.setItem(parseType(schema.getItems()));

        return arrayTypeDescriptor;
    }

    private BoolTypeDescriptor parseBoolean(final Schema<?> schema){

        return store.create(BoolTypeDescriptor.class);
    }

    private EnumStringTypeDescriptor parseEnum(final Schema<?> schema){

        EnumStringTypeDescriptor enumStringTypeDescriptor = store.create(
                EnumStringTypeDescriptor.class);
        enumStringTypeDescriptor.setValues(parseEnumValues(schema.getEnum()));

        return enumStringTypeDescriptor;
    }

    private List<EnumValueDescriptor> parseEnumValues(List<?> enums){
        List<EnumValueDescriptor> ret = new ArrayList<>();

        enums.forEach( val -> {
            EnumValueDescriptor enumValueDescriptor = store.create(EnumValueDescriptor.class);
            enumValueDescriptor.setEnumName(val.toString());
            ret.add(enumValueDescriptor);
        });

        return ret;
    }

    private IntegerTypeDescriptor parseInteger(final Schema<?> schema){

        return parseFormat(schema, store.create(
                IntegerTypeDescriptor.class));
    }

    private NullTypeDescriptor parseNull(final Schema<?> schema){

        return store.create(NullTypeDescriptor.class);
    }

    private NumberTypeDescriptor parseNumber(final Schema<?> schema){

        return parseFormat(schema, store.create(
                NumberTypeDescriptor.class));
    }

    private ReferenceTypeDescriptor parseReference(String ref){

        ReferenceTypeDescriptor referenceTypeDescriptor = store.create(
                ReferenceTypeDescriptor.class);

        referenceTypeDescriptor.setReference(resolver.resolve(ref));

        return referenceTypeDescriptor;
    }

    private StringTypeDescriptor parseString(final Schema<?> schema) {

        if (schema.getEnum() == null || schema.getEnum().isEmpty()) {
            return parseFormat(schema, store.create(
                    StringTypeDescriptor.class));
        } else {
            return parseEnum(schema);
        }
    }

    private TypeDescriptor parseObjectOrReference(Schema<?> x){
        if (x.get$ref() != null)
            return parseReference(x.get$ref());
        else
            return parseType(x);
    }

    private <D extends TypeDescriptor> D parseFormat(Schema<?> schema, D typeDescriptor) {

        if (schema.getFormat() != null)
            typeDescriptor.setFormat(schema.getFormat());

        return typeDescriptor;
    }

}
