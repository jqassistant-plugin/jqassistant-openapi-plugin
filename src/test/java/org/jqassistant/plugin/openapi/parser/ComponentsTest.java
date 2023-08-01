package org.jqassistant.plugin.openapi.parser;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.media.Encoding;
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

        assertThat(requestBodies.get(0).getMediaTypeObjects()).hasSize(1);
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
        assertThat(responses).hasSize(2);
    }

    @Test
    void testMediaTypeObjects(){
        ResponseDescriptor response404 = getResponseByName("418");
        assertThat(response404.getMediaTypeObjects()).hasSize(3);

        MediaTypeObjectDescriptor mtoExample = getMediaTypeObjectByName("multipart/form-data_example");
        assertThat(mtoExample.getMediaType()).isEqualTo("multipart/form-data_example");
        assertThat(mtoExample.getSchema()).isNotNull();
        assertThat(mtoExample.getExample()).isNotNull();
        assertThat(mtoExample.getExamples()).isEmpty();
        assertThat(mtoExample.getEncodings()).hasSize(2);

        MediaTypeObjectDescriptor mtoExamples = getMediaTypeObjectByName("multipart/form-data_examples");
        assertThat(mtoExamples.getMediaType()).isEqualTo("multipart/form-data_examples");
        assertThat(mtoExamples.getSchema()).isNotNull();
        assertThat(mtoExamples.getExample()).isNull();
        assertThat(mtoExamples.getExamples()).hasSize(2);
        assertThat(mtoExamples.getEncodings()).isEmpty();

        MediaTypeObjectDescriptor mtoEmpty = getMediaTypeObjectByName("multipart/form-data_empty");
        assertThat(mtoEmpty.getMediaType()).isEqualTo("multipart/form-data_empty");
        assertThat(mtoEmpty.getSchema()).isNull();
        assertThat(mtoEmpty.getExample()).isNull();
        assertThat(mtoEmpty.getExamples()).isEmpty();
        assertThat(mtoEmpty.getEncodings()).isEmpty();
    }

    @Test
    void testEncodings(){
        MediaTypeObjectDescriptor mto = contract.getComponents().getRequestBodies().get(0).getMediaTypeObjects().get(0);
        assertThat(mto.getEncodings()).hasSize(2);

        EncodingDescriptor encoding1 = mto.getEncodings().get(0);
        assertThat(encoding1.getPropertyName()).isEqualTo("property1");
        assertThat(encoding1.getContentType()).isEqualTo("application/xml; charset=utf-8");
        assertThat(encoding1.getHeaders()).hasSize(1);
        assertThat(encoding1.getExplode()).isTrue();
        assertThat(encoding1.getAllowsReserved()).isTrue();
        assertThat(encoding1.getStyle()).isEqualTo(Encoding.StyleEnum.valueOf("SPACE_DELIMITED"));

        EncodingDescriptor encoding2 = mto.getEncodings().get(1);
        assertThat(encoding2.getPropertyName()).isEqualTo("property2");
        assertThat(encoding2.getContentType()).isNull();
        assertThat(encoding2.getHeaders()).isEmpty();
        assertThat(encoding2.getExplode()).isFalse();
        assertThat(encoding2.getAllowsReserved()).isFalse();
        assertThat(encoding2.getStyle()).isEqualTo(Encoding.StyleEnum.valueOf("FORM"));
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

    private ResponseDescriptor getResponseByName(String name){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses)
            if(response.getStatusCode().equals(name))
                return response;
        return null;
    }

    private MediaTypeObjectDescriptor getMediaTypeObjectByName(String name){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses)
            for(MediaTypeObjectDescriptor mediaType: response.getMediaTypeObjects())
                if(mediaType.getMediaType().equals(name))
                    return mediaType;
        return null;
    }
}
