package org.jqassistant.plugin.openapi.impl.jsonschema;

import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.xo.api.Query;
import com.buschmais.xo.api.Query.Result.CompositeRowObject;
import com.buschmais.xo.api.ResultIterator;
import io.swagger.v3.oas.models.media.Schema;
import org.jqassistant.plugin.openapi.api.model.jsonschema.*;

import java.util.ArrayList;
import java.util.List;

public class JSONSchemaObjectReader {

    private final Store store;
    public JSONSchemaObjectReader(Store store){
        this.store = store;
    }

    PropertyDescriptor parseProperty(final String name, final Schema<?> schema) {

        PropertyDescriptor val;

        if (schema.getType() == null)
            return parseReference(schema.get$ref());

        switch (schema.getType()) {
            case ArrayPropertyDescriptor.TYPE_NAME:
                val = parseArray(schema);
                break;
            case BoolPropertyDescriptor.TYPE_NAME:
                val = parseBoolean(schema);
                break;
            case IntegerPropertyDescriptor.TYPE_NAME:
                val = parseInteger(schema);
                break;
            case NullPropertyDescriptor.TYPE_NAME:
                val = parseNull(schema);
                break;
            case NumberPropertyDescriptor.TYPE_NAME:
                val = parseNumber(schema);
                break;
            case ObjectPropertyDescriptor.TYPE_NAME:
                val = parseObject(name, schema);
                break;
            case StringPropertyDescriptor.TYPE_NAME:
                val = parseString(schema);
                break;
            default:
                throw new RuntimeException("Unknown Type \"" + schema.getType() + "\" in Schema: " + schema.getName());
        }

        val.setName(name);

        return val;
    }

    public ObjectPropertyDescriptor parseObject(final String name, final Schema<?> schema){

        ObjectPropertyDescriptor objectPropertyDescriptor = store.create(ObjectPropertyDescriptor.class);
        objectPropertyDescriptor.setName(name);

        for (String key : schema.getProperties().keySet()) {
            PropertyDescriptor prop = parseProperty(key, schema.getProperties().get(key));
            objectPropertyDescriptor.getProperties().add(prop);
        }

        return objectPropertyDescriptor;

    }

    ArrayPropertyDescriptor parseArray(final Schema<?> schema){

        ArrayPropertyDescriptor arrayPropertyDescriptor = parseDescription(schema, ArrayPropertyDescriptor.class);
        arrayPropertyDescriptor.setItem(resolveSchema(schema.getItems().get$ref()));

        return arrayPropertyDescriptor;
    }

    BoolPropertyDescriptor parseBoolean(final Schema<?> schema){

        return parseDescription(schema, BoolPropertyDescriptor.class);
    }

    EnumStringPropertyDescriptor parseEnum(final Schema<?> schema){ // TODO

        EnumStringPropertyDescriptor enumStringPropertyDescriptor = parseDescription(schema, EnumStringPropertyDescriptor.class);

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

        return parseDescriptionAndFormat(schema, IntegerPropertyDescriptor.class);
    }

    NullPropertyDescriptor parseNull(final Schema<?> schema){

        return parseDescription(schema, NullPropertyDescriptor.class);
    }

    NumberPropertyDescriptor parseNumber(final Schema<?> schema){

        return parseDescriptionAndFormat(schema,NumberPropertyDescriptor.class);
    }
    
    ReferencePropertyDescriptor parseReference(String ref){
        ReferencePropertyDescriptor referencePropertyDescriptor = store.create(ReferencePropertyDescriptor.class);

        referencePropertyDescriptor.setReference(resolveSchema(ref));

        return referencePropertyDescriptor;
    }

    StringPropertyDescriptor parseString(final Schema<?> schema) {

        return parseDescriptionAndFormat(schema, StringPropertyDescriptor.class);
    }

    final <D extends PropertyDescriptor> D parseDescription(Schema<?> schema, Class<D> clazz){

        D propertyDescriptor = store.create(clazz);

        if (schema.getDescription() != null)
            propertyDescriptor.setDescription(schema.getDescription());

        return propertyDescriptor;
    }

    final <D extends PropertyDescriptor> D parseDescriptionAndFormat(Schema<?> schema, Class<D> clazz) {
        D propertyDescriptor = parseDescription(schema, clazz);

        if (schema.getFormat() != null)
            propertyDescriptor.setFormat(schema.getFormat());

        return propertyDescriptor;
    }

    SchemaDescriptor resolveSchema(String ref){
        Query.Result<CompositeRowObject> res = store.executeQuery(QuerryWrapper.getSchemaWithName(ref));

        SchemaDescriptor schemaDescriptor = null;

        if (res.hasResult()){
            ResultIterator<CompositeRowObject> it = res.iterator();
            CompositeRowObject compositeRowObject = null;

            while (it.hasNext()){ // Present result, get Existing Schema
                if (schemaDescriptor != null){
                    throw new RuntimeException("Too many results!");
                }

                compositeRowObject = it.next();

                if (compositeRowObject.getColumns().size() > 1){
                    throw new RuntimeException("Too many results!");
                }

                schemaDescriptor = compositeRowObject.get(compositeRowObject.getColumns().get(0), SchemaDescriptor.class);
            }
        } else { // No Present Schema, create new Schema
            System.out.println("UHM .. .yeh");
        }

        return schemaDescriptor;
    }

}
