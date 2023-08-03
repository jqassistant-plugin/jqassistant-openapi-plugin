package org.jqassistant.plugin.openapi.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
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
        assertThat(responses).hasSize(4);

        ResponseDescriptor resDefault = getResponse("default");
        assertThat(resDefault.getIsDefault()).isTrue();
        assertThat(resDefault.getStatusCode()).isNull();
        assertThat(resDefault.getDescription()).isEqualTo("a default response with all fields");
        assertThat(resDefault.getHeaders()).hasSize(1);
        assertThat(resDefault.getMediaTypeObjects()).hasSize(1);
        assertThat(resDefault.getLinks()).hasSize(1);

        ResponseDescriptor resEmpty = getResponse("433");
        assertThat(resEmpty.getIsDefault()).isFalse();
        assertThat(resEmpty.getStatusCode()).isEqualTo("433");
        assertThat(resEmpty.getDescription()).isNull();
        assertThat(resEmpty.getHeaders()).isEmpty();
        assertThat(resEmpty.getMediaTypeObjects()).isEmpty();
        assertThat(resEmpty.getLinks()).isEmpty();

        ResponseDescriptor resNoFields = getResponse("433");
        assertThat(resNoFields.getIsDefault()).isFalse();
        assertThat(resNoFields.getStatusCode()).isEqualTo("433");
        assertThat(resNoFields.getDescription()).isNull();
        assertThat(resNoFields.getHeaders()).isEmpty();
        assertThat(resNoFields.getMediaTypeObjects()).isEmpty();
        assertThat(resNoFields.getLinks()).isEmpty();
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

    private ResponseDescriptor getResponse(String statusCodeOrDefault){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses) {
            if (response.getIsDefault() && statusCodeOrDefault.equals("default"))
                return response;
            if (!statusCodeOrDefault.equals("default") && response.getStatusCode() != null && response.getStatusCode().equals(statusCodeOrDefault))
                return response;
        }
        return null;
    }
}
