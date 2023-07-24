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
    void schemaWithNativeTypeTest(){
        SchemaDescriptor schemaDescriptor = getSchemaWithName("IntegerSchema");

        assertThat(schemaDescriptor).isNotNull();
        assertThat(schemaDescriptor.getName()).isEqualTo("IntegerSchema");

        PropertyDescriptor propertyDescriptor = schemaDescriptor.getObject();

        assertThat(propertyDescriptor).isNotNull();
        assertThat(propertyDescriptor.getName()).isEqualTo("IntegerSchema");
        assertThat(propertyDescriptor).isInstanceOf(IntegerPropertyDescriptor.class);
    }

    @Test
    void schemaWithObjectTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithObject");
        assertThat(properties)
                .isNotNull()
                .hasSize(1);

        PropertyDescriptor propertyDescriptor1 = properties.get(0);

        assertThat(propertyDescriptor1).isNotNull();
        assertThat(propertyDescriptor1.getName()).isEqualTo("test");
        assertThat(propertyDescriptor1).isInstanceOf(IntegerPropertyDescriptor.class);
    }

    @Test
    void schemaWithObjectAndAllTypesTest(){
        List<PropertyDescriptor> properties = getPropertyListFromSchemaWithName("SchemaWithObjectAndAllTypes");

        for (PropertyDescriptor propertyDescriptor : properties){
            switch (propertyDescriptor.getName()){
                case "ArrayType":
                    assertThat(propertyDescriptor).isInstanceOf(ArrayPropertyDescriptor.class);
                    ArrayPropertyDescriptor prop = (ArrayPropertyDescriptor) propertyDescriptor;
                    assertThat(prop.getItem()).isNotNull();
                    assertThat(prop.getItem()).isInstanceOf(IntegerPropertyDescriptor.class);
                    break;
                case "BoolType":
                    assertThat(propertyDescriptor).isInstanceOf(BoolPropertyDescriptor.class);
                    break;
                case "StringType":
                    assertThat(propertyDescriptor).isInstanceOf(StringPropertyDescriptor.class);
                    break;
                case "IntegerType":
                    assertThat(propertyDescriptor).isInstanceOf(IntegerPropertyDescriptor.class);
                    break;
                case "NullType":
                    assertThat(propertyDescriptor).isInstanceOf(NullPropertyDescriptor.class);
                    break;
                case "NumberType":
                    assertThat(propertyDescriptor).isInstanceOf(NumberPropertyDescriptor.class);
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
            assertThat(propertyDescriptor).isInstanceOf(ArrayPropertyDescriptor.class);
            ArrayPropertyDescriptor arrayPropertyDescriptor = (ArrayPropertyDescriptor) propertyDescriptor;

            if (arrayPropertyDescriptor.getName().equals("ArrayWithSimpleType")){
                assertThat(arrayPropertyDescriptor.getItem()).isInstanceOf(IntegerPropertyDescriptor.class);
            } else if (arrayPropertyDescriptor.getName().equals("ArrayWithReference")){
                assertThat(arrayPropertyDescriptor.getItem()).isInstanceOf(ReferencePropertyDescriptor.class);
                SchemaDescriptor refSchema = ((ReferencePropertyDescriptor) arrayPropertyDescriptor.getItem()).getReference();

                assertThat(refSchema).isNotNull();
                assertThat(refSchema.getName()).isEqualTo("SchemaWithObject");
                assertThat(refSchema.getObject()).isInstanceOf(ObjectPropertyDescriptor.class);
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
                assertThat(propertyDescriptor).isInstanceOf(StringPropertyDescriptor.class);
            } else if (propertyDescriptor.getName().equals("EnumType")){
                assertThat(propertyDescriptor).isInstanceOf(EnumStringPropertyDescriptor.class);
                EnumStringPropertyDescriptor enumProp = (EnumStringPropertyDescriptor) propertyDescriptor;

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
            assertThat(propertyDescriptor).isInstanceOf(IntegerPropertyDescriptor.class);

            IntegerPropertyDescriptor intProperty = (IntegerPropertyDescriptor) propertyDescriptor;

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
            assertThat(propertyDescriptor).isInstanceOf(NumberPropertyDescriptor.class);

            NumberPropertyDescriptor intProperty = (NumberPropertyDescriptor) propertyDescriptor;

            if (propertyDescriptor.getName().equals("DoubleValue")){
                assertThat(intProperty.getFormat()).isEqualTo("double");
            } else if (propertyDescriptor.getName().equals("FloatValue")){
                assertThat(intProperty.getFormat()).isEqualTo("float");
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

        SchemaDescriptor refSchema = ((ReferencePropertyDescriptor) properties.get(0)).getReference();

        assertThat(refSchema).isNotNull();
        assertThat(refSchema.getName()).isEqualTo("SchemaWithObject");
        assertThat(refSchema.getObject()).isInstanceOf(ObjectPropertyDescriptor.class);
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

        PropertyDescriptor propertyDescriptor = schemaDescriptor.getObject();

        assertThat(propertyDescriptor).isNotNull();
        assertThat(propertyDescriptor.getName()).isEqualTo(schemaName);
        assertThat(propertyDescriptor).isInstanceOf(ObjectPropertyDescriptor.class);

        return  ((ObjectPropertyDescriptor) propertyDescriptor).getProperties();
    }

}