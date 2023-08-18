package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ComponentsDescriptor;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.jsonschema.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JsonSchemaTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @BeforeEach
    void init(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-jsonschema.yaml");
        contract = getScanner().scan(file, "/example-jsonschema.yaml", DefaultScope.NONE);

        store.beginTransaction();

        assertThat(contract).isNotNull();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void readyAnySchemaTest(){
        ComponentsDescriptor componentsDescriptor = contract.getComponents();
        assertThat(componentsDescriptor).isNotNull();

        assertThat(componentsDescriptor.getSchemas()).isNotNull();
        assertThat(componentsDescriptor.getSchemas()).hasSizeGreaterThan(1);
    }

    @Test
    void schemaWithEmptyPropertyTest(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithEmptyProperty");

        TypeDescriptor typeDescriptor = schemaDescriptor.getIsType();

        assertThat(typeDescriptor)
                .isNotNull()
                .isInstanceOf(ObjectTypeDescriptor.class);

        ObjectTypeDescriptor obj = (ObjectTypeDescriptor) typeDescriptor;

        assertThat(obj.getProperties())
                .isEmpty();
    }

    @Test
    void schemaWithOtherTypeThenObjectTest(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithOtherTypeThenObject");

        assertThat(schemaDescriptor).isNotNull();

        TypeDescriptor typeDescriptor = schemaDescriptor.getIsType();

        assertThat(typeDescriptor)
                .isNotNull()
                .isInstanceOf(IntegerTypeDescriptor.class);

    }
    @Test
    void schemaWithNativeTypeTest(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("IntegerSchema");

        assertThat(schemaDescriptor).isNotNull();
        assertThat(schemaDescriptor.getName()).isEqualTo("IntegerSchema");

        TypeDescriptor typeDescriptor = schemaDescriptor.getIsType();

        assertThat(typeDescriptor)
                .isNotNull()
                .isInstanceOf(IntegerTypeDescriptor.class);

        assertThat(schemaDescriptor.getExternalDocs()).isNotNull(); // TODO WRONG TEST?
    }

    @Test
    void schemaWithObjectTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithObject");
        assertThat(properties)
                .isNotNull()
                .hasSize(1);

        PropertyDescriptor propertyDescriptor = properties.get(0);

        assertThat(propertyDescriptor).isNotNull();
        assertThat(propertyDescriptor.getName()).isEqualTo("test");
        assertThat(propertyDescriptor.getType()).isInstanceOf(IntegerTypeDescriptor.class);
    }

    @Test
    void schemaWithObjectAndAllTypesTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithObjectAndAllTypes");

        for (PropertyDescriptor propertyDescriptor : properties){
            switch (propertyDescriptor.getName()){
                case "ArrayType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(ArrayTypeDescriptor.class);
                    ArrayTypeDescriptor prop = (ArrayTypeDescriptor) propertyDescriptor.getType();
                    assertThat(prop.getItem()).isNotNull();
                    assertThat(prop.getItem()).isInstanceOf(IntegerTypeDescriptor.class);
                    break;
                case "BoolType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(BoolTypeDescriptor.class);
                    break;
                case "StringType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(StringTypeDescriptor.class);
                    break;
                case "IntegerType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(IntegerTypeDescriptor.class);
                    break;
                case "NullType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(NullTypeDescriptor.class);
                    break;
                case "NumberType":
                    assertThat(propertyDescriptor.getType()).isInstanceOf(NumberTypeDescriptor.class);
                    break;
                default:
                    Assertions.fail("unexpected Type");
            }
        }
    }

    @Test
    void schemaWithArraysTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithArrays");

        assertThat(properties)
                .isNotNull()
                .hasSize(2);

        for (PropertyDescriptor propertyDescriptor : properties){

            TypeDescriptor type = propertyDescriptor.getType();
            assertThat(type)
                    .isNotNull()
                    .isInstanceOf(ArrayTypeDescriptor.class);

            ArrayTypeDescriptor arrayTypeDescriptor = (ArrayTypeDescriptor) type;

            if (propertyDescriptor.getName().equals("ArrayWithSimpleType")){
                assertThat(arrayTypeDescriptor.getItem()).isInstanceOf(IntegerTypeDescriptor.class);
            } else if (propertyDescriptor.getName().equals("ArrayWithReference")){
                assertThat(arrayTypeDescriptor.getItem()).isInstanceOf(ReferenceTypeDescriptor.class);
                SchemaDescriptor refSchema = ((ReferenceTypeDescriptor) arrayTypeDescriptor.getItem()).getReference();

                assertThat(refSchema).isNotNull();
                assertThat(refSchema.getName()).isEqualTo("SchemaWithObject");
                assertThat(refSchema.getIsType()).isInstanceOf(ObjectTypeDescriptor.class);
            } else {
                Assertions.fail("unexpected property found");
            }
        }
    }

    @Test
    void schemaWithStringTypesTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithStringTypes");

        assertThat(properties)
                .isNotNull()
                .hasSize(2);

        for (PropertyDescriptor propertyDescriptor : properties){

            if (propertyDescriptor.getName().equals("StringType")){
                assertThat(propertyDescriptor.getType()).isInstanceOf(StringTypeDescriptor.class);
            } else if (propertyDescriptor.getName().equals("EnumType")){
                TypeDescriptor type = propertyDescriptor.getType();
                assertThat(type).isInstanceOf(EnumStringTypeDescriptor.class);
                EnumStringTypeDescriptor enumProp = (EnumStringTypeDescriptor) type;

                List<EnumValueDescriptor> vals = enumProp.getValues();
                assertThat(vals).isNotNull();
                assertThat(vals).hasSize(3);

                Set<String> actual = new HashSet<>();
                vals.forEach(val -> actual.add(val.getEnumName()));

                assertThat(actual).containsExactlyInAnyOrder("foo", "baa", "baz");

            } else {
                Assertions.fail("unexpected property found");
            }
        }
    }

    @Test
    void schemaWithIntegerTypesTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithIntegerTypes");

        assertThat(properties)
                .isNotNull()
                .hasSize(2);

        for (PropertyDescriptor propertyDescriptor : properties){

            TypeDescriptor type = propertyDescriptor.getType();
            assertThat(type)
                    .isNotNull()
                    .isInstanceOf(IntegerTypeDescriptor.class);

            IntegerTypeDescriptor intProperty = (IntegerTypeDescriptor) type;

            if (propertyDescriptor.getName().equals("Integer32")){
                assertThat(intProperty.getFormat()).isEqualTo("int32");
            } else if (propertyDescriptor.getName().equals("Integer64")){
                assertThat(intProperty.getFormat()).isEqualTo("int64");
            } else {
                Assertions.fail("unexpected property found");
            }
        }
    }

    @Test
    void schemaWithNumberTypesTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithNumberTypes");

        assertThat(properties)
                .isNotNull()
                .hasSize(2);

        for (PropertyDescriptor propertyDescriptor : properties){

            TypeDescriptor type = propertyDescriptor.getType();
            assertThat(type)
                    .isNotNull()
                    .isInstanceOf(NumberTypeDescriptor.class);

            NumberTypeDescriptor numProperty = (NumberTypeDescriptor) type;

            if (propertyDescriptor.getName().equals("DoubleValue")){
                assertThat(numProperty.getFormat()).isEqualTo("double");
            } else if (propertyDescriptor.getName().equals("FloatValue")){
                assertThat(numProperty.getFormat()).isEqualTo("float");
            } else {
                Assertions.fail("unexpected property found");
            }
        }
    }

    @Test
    void schemaWithReferenceTypeTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithReferenceType");

        assertThat(properties)
                .isNotNull()
                .hasSize(1);

        TypeDescriptor type = properties.get(0).getType();

        assertThat(type)
                .isNotNull()
                .isInstanceOf(ReferenceTypeDescriptor.class);

        SchemaDescriptor refSchema = ((ReferenceTypeDescriptor) type).getReference();

        assertThat(refSchema).isNotNull();
        assertThat(refSchema.getName()).isEqualTo("SchemaWithObject");
        assertThat(refSchema.getIsType()).isInstanceOf(ObjectTypeDescriptor.class);
    }

    @Test
    void SchemaWithInherentAllOf(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithInherentAllOf");

        List<TypeDescriptor> allOffs = schemaDescriptor.getAllOfSchemas();

        assertThat(allOffs)
                .isNotNull()
                .hasSize(2);

        for (TypeDescriptor type : allOffs){
            assertThat(type).isInstanceOfAny(ReferenceTypeDescriptor.class, ObjectTypeDescriptor.class);
        }
    }

    @Test
    void SchemaWithInherentOneOf(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithInherentOneOf");

        List<TypeDescriptor> oneOffs = schemaDescriptor.getOneOfSchemas();

        assertThat(oneOffs)
                .isNotNull()
                .hasSize(2);

        for (TypeDescriptor type : oneOffs){
            assertThat(type).isInstanceOfAny(ReferenceTypeDescriptor.class, ObjectTypeDescriptor.class);
        }
    }

    @Test
    void SchemaWithInherentAnyOf(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithInherentAnyOf");

        List<TypeDescriptor> anyOffs = schemaDescriptor.getAnyOfSchemas();

        assertThat(anyOffs)
                .isNotNull()
                .hasSize(2);

        for (TypeDescriptor type : anyOffs){
            assertThat(type).isInstanceOfAny(ReferenceTypeDescriptor.class, ObjectTypeDescriptor.class);
        }
    }

    @Test
    void schemaWithMappingDiscriminatorTest() {
        SchemaDescriptor schemaDescriptor = getSchemaWithName("SchemaWithMappingDiscriminator");

        assertThat(schemaDescriptor).isNotNull();

        DiscriminatorDescriptor discriminatorDescriptor = schemaDescriptor.getDiscriminator();

        assertThat(discriminatorDescriptor).isNotNull();

        List<DiscriminatorMappingDescriptor> mapping = discriminatorDescriptor.getMapping();

        assertThat(mapping).hasSize(2);

        for (int i = 0; i <2; i++) {
            assertThat(mapping.get(i).getKey())
                    .isNotNull()
                    .isIn(Arrays.asList("foo", "baz"));
            assertThat(mapping.get(i).getValue())
                    .isNotNull()
                    .isIn(Arrays.asList("BAA", "#/components/schemas/SchemaWithObject"));
        }
    }

    private SchemaDescriptor getSchemaWithName(String schemaName){
        for (SchemaDescriptor schemaDescriptor : contract.getComponents().getSchemas()){
            if (schemaDescriptor.getName().equals(schemaName))
                return schemaDescriptor;
        }

        return null;
    }

    private List<PropertyDescriptor> getPropertyListFromSchemaWithName(String schemaName){
        SchemaDescriptor schemaDescriptor = getSchemaWithName(schemaName);

        assertThat(schemaDescriptor).isNotNull();
        assertThat(schemaDescriptor.getName()).isEqualTo(schemaName);

        TypeDescriptor typeDescriptor = schemaDescriptor.getIsType();

        assertThat(typeDescriptor).isNotNull();
        assertThat(typeDescriptor).isInstanceOf(ObjectTypeDescriptor.class);

        return  ((ObjectTypeDescriptor) typeDescriptor).getProperties();
    }

}