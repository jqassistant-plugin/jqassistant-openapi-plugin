package org.jqassistant.plugin.openapi.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.jqassistant.plugin.openapi.api.model.*;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentsTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @BeforeEach
    void init(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-components.yaml");
        contract = getScanner().scan(file, "/example-components.yaml", DefaultScope.NONE);

        store.beginTransaction();

        assertThat(contract).isNotNull();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void testRequestBodies() {
        List<RequestBodyDescriptor> requestBodies = contract.getComponents().getRequestBodies();
        assertThat(requestBodies).hasSize(1);
    }

    @Test
    void testHeaders() {
        List<HeaderDescriptor> headers = contract.getComponents().getHeaders();
        assertThat(headers).hasSize(1);
    }

    @Test
    void testSecuritySchemes() {
        List<SecuritySchemeDescriptor> securitySchemes = contract.getComponents().getSecuritySchemas();
        assertThat(securitySchemes).hasSize(1);
    }

    @Test
    void testLinks() {
        List<LinkDescriptor> links = contract.getComponents().getLinks();
        assertThat(links).hasSize(1);
    }

    @Test
    void testCallbacks() {
        List<CallbackDescriptor> callbacks = contract.getComponents().getCallBacks();
        assertThat(callbacks).hasSize(1);
    }

    @Test
    void testExamples() {
        List<ExampleDescriptor> examples = contract.getComponents().getExamples();
        assertThat(examples).hasSize(1);
    }

    @Test
    void testResponses() {
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        assertThat(responses).hasSize(1);
    }

    @Test
    void testParameterRelation(){
        List<ParameterDescriptor> parameters = contract.getComponents().getParameters();
        assertThat(parameters).hasSize(4);
    }

    @Test
    void testRichParameters() {
        ParameterDescriptor paramAllFieldsExample = getParamByName("param_all_fields_example");
        assertThat(paramAllFieldsExample.getName()).isEqualTo("param_all_fields_example");
        assertThat(paramAllFieldsExample.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.HEADER);
        assertThat(paramAllFieldsExample.getDescription()).isEqualTo("token to be passed as a header");
        assertThat(paramAllFieldsExample.getIsRequired()).isTrue();
        assertThat(paramAllFieldsExample.getIsDeprecated()).isTrue();
        assertThat(paramAllFieldsExample.getAllowsEmptyValue()).isNull(); // null when location is HEADER
        assertThat(paramAllFieldsExample.getStyle()).isEqualTo(Parameter.StyleEnum.SIMPLE);
        assertThat(paramAllFieldsExample.getExplode()).isTrue();
        assertThat(paramAllFieldsExample.getAllowsReserved()).isTrue();
        assertThat(paramAllFieldsExample.getSchema()).isNotNull();
        assertThat(paramAllFieldsExample.getExample()).isNotNull();
        assertThat(paramAllFieldsExample.getExamples()).isEmpty();

        ParameterDescriptor paramAllFieldsExamples = getParamByName("param_all_fields_examples");
        assertThat(paramAllFieldsExamples.getName()).isEqualTo("param_all_fields_examples");
        assertThat(paramAllFieldsExamples.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.QUERY);
        assertThat(paramAllFieldsExamples.getDescription()).isEqualTo("token to be passed as a header");
        assertThat(paramAllFieldsExamples.getIsRequired()).isFalse();
        assertThat(paramAllFieldsExamples.getIsDeprecated()).isFalse();
        assertThat(paramAllFieldsExamples.getAllowsEmptyValue()).isFalse(); // null when location is HEADER
        assertThat(paramAllFieldsExamples.getStyle()).isEqualTo(Parameter.StyleEnum.SIMPLE);
        assertThat(paramAllFieldsExamples.getExplode()).isFalse();
        assertThat(paramAllFieldsExamples.getAllowsReserved()).isFalse();
        assertThat(paramAllFieldsExamples.getSchema()).isNotNull();
        assertThat(paramAllFieldsExamples.getExample()).isNull();
        assertThat(paramAllFieldsExamples.getExamples()).hasSize(2);
    }

    @Test
    void testEmptyParameters(){
        ParameterDescriptor paramEmptyFieldsExamples = getParamByName("param_empty_fields");
        assertThat(paramEmptyFieldsExamples.getName()).isEqualTo("param_empty_fields");
        assertThat(paramEmptyFieldsExamples.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.HEADER);
        assertThat(paramEmptyFieldsExamples.getDescription()).isNull();
        assertThat(paramEmptyFieldsExamples.getIsRequired()).isFalse();
        assertThat(paramEmptyFieldsExamples.getIsDeprecated()).isNull();
        assertThat(paramEmptyFieldsExamples.getAllowsEmptyValue()).isNull(); // null when location is HEADER
        assertThat(paramEmptyFieldsExamples.getStyle()).isEqualTo(Parameter.StyleEnum.SIMPLE);
        assertThat(paramEmptyFieldsExamples.getExplode()).isFalse();
        assertThat(paramEmptyFieldsExamples.getAllowsReserved()).isFalse();
        assertThat(paramEmptyFieldsExamples.getSchema()).isNull();
        assertThat(paramEmptyFieldsExamples.getExample()).isNull();
        assertThat(paramEmptyFieldsExamples.getExamples()).isEmpty();

        ParameterDescriptor paramNoFieldsExamples = getParamByName("param_no_fields");
        assertThat(paramNoFieldsExamples.getName()).isEqualTo("param_no_fields");
        assertThat(paramNoFieldsExamples.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.HEADER);
        assertThat(paramNoFieldsExamples.getDescription()).isNull();
        assertThat(paramNoFieldsExamples.getIsRequired()).isFalse();
        assertThat(paramNoFieldsExamples.getIsDeprecated()).isNull();
        assertThat(paramNoFieldsExamples.getAllowsEmptyValue()).isNull(); // null when location is HEADER
        assertThat(paramNoFieldsExamples.getStyle()).isEqualTo(Parameter.StyleEnum.SIMPLE);
        assertThat(paramNoFieldsExamples.getExplode()).isFalse();
        assertThat(paramNoFieldsExamples.getAllowsReserved()).isFalse();
        assertThat(paramNoFieldsExamples.getSchema()).isNull();
        assertThat(paramNoFieldsExamples.getExample()).isNull();
        assertThat(paramNoFieldsExamples.getExamples()).isEmpty();
    }

    @Test
    void testSchemas() {
        List<SchemaDescriptor> schemas = contract.getComponents().getSchemas();
        assertThat(schemas).hasSize(1);
    }

    private ParameterDescriptor getParamByName(String name){
        List<ParameterDescriptor> params = contract.getComponents().getParameters();
        for(ParameterDescriptor param: params)
            if(param.getName().equals(name))
                return param;
        return null;
    }
}
