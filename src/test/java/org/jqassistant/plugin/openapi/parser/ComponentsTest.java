package org.jqassistant.plugin.openapi.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.headers.Header;
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
        assertThat(headers).hasSize(3);
    }

    @Test
    void testHeaderProperties() {
        // header all props set and examples field
        HeaderDescriptor headerAllPropsExamples = getHeaderByName("header_all_props");
        assertThat(headerAllPropsExamples).isNotNull();
        assertThat(headerAllPropsExamples.getDescription()).isEqualTo("This is an header with all possible props set");
        assertThat(headerAllPropsExamples.getIsRequired()).isTrue();
        assertThat(headerAllPropsExamples.getIsDeprecated()).isTrue();
        assertThat(headerAllPropsExamples.getStyle()).isEqualTo(Header.StyleEnum.valueOf("SIMPLE"));
        assertThat(headerAllPropsExamples.getExplode()).isTrue();
        assertThat(headerAllPropsExamples.getSchema()).isNotNull();
        assertThat(headerAllPropsExamples.getExamples()).hasSize(2);
        assertThat(headerAllPropsExamples.getContent()).hasSize(1);

        // header example props set
        HeaderDescriptor headerAllPropsExample = getHeaderByName("header_example_prop");
        assertThat(headerAllPropsExample.getDescription()).isEqualTo("This is an header with the example prop set");
        assertThat(headerAllPropsExample.getSchema()).isNotNull();

        // header all props empty
        HeaderDescriptor headerEmptyProps = getHeaderByName("header_empty_props");
        assertThat(headerEmptyProps).isNotNull();
        assertThat(headerEmptyProps.getDescription()).isNull();
        assertThat(headerEmptyProps.getIsRequired()).isFalse();
        assertThat(headerEmptyProps.getIsDeprecated()).isFalse();
        assertThat(headerEmptyProps.getStyle()).isEqualTo(Header.StyleEnum.valueOf("SIMPLE"));
        assertThat(headerEmptyProps.getExplode()).isFalse();
        assertThat(headerEmptyProps.getExample()).isNull();
        assertThat(headerEmptyProps.getSchema()).isNull();
        assertThat(headerEmptyProps.getExamples()).isEmpty();
        assertThat(headerEmptyProps.getContent()).isEmpty();
    }

    private HeaderDescriptor getHeaderByName(String name){
        List<HeaderDescriptor> headers = contract.getComponents().getHeaders();
        for (HeaderDescriptor header : headers){
            if (header.getName().equals(name))
                return header;
        }
        return null;
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
    void testParameters() {
        List<ParameterDescriptor> parameters = contract.getComponents().getParameters();
        assertThat(parameters).hasSize(1);
    }

    @Test
    void testSchemas() {
        List<SchemaDescriptor> schemas = contract.getComponents().getSchemas();
        assertThat(schemas).hasSize(1);
    }
}
