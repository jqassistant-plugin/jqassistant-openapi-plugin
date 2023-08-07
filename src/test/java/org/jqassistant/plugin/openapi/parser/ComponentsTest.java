package org.jqassistant.plugin.openapi.parser;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Encoding;
import org.jqassistant.plugin.openapi.api.model.*;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(links).hasSize(4);
    }

    @Test
    void testRichLinks(){
        LinkDescriptor richLinkOperationId = getLinkByName("rich_link_with_operationId");
        assertThat(richLinkOperationId.getOperationId()).isEqualTo("getUserAddress");
        assertThat(richLinkOperationId.getOperationRef()).isNull();
        assertThat(richLinkOperationId.getParameters()).hasSize(1);
        assertThat(richLinkOperationId.getParameters().get(0).getName()).isEqualTo("userId");
        assertThat(richLinkOperationId.getParameters().get(0).getValue()).isEqualTo("$request.path.id");
        assertThat(richLinkOperationId.getRequestBody()).isEqualTo("$request.body#/user/uuid");
        assertThat(richLinkOperationId.getDescription()).isEqualTo("description");
        assertThat(richLinkOperationId.getServer()).isNotNull();

        LinkDescriptor richLinkOperationRef = getLinkByName("rich_link_with_operationRef");
        assertThat(richLinkOperationRef.getOperationId()).isNull();
        assertThat(richLinkOperationRef.getOperationRef()).isEqualTo("#/paths/~12.0~1repositories~1{username}/get");
        assertThat(richLinkOperationRef.getParameters()).hasSize(2);
        assertThat(richLinkOperationRef.getRequestBody()).isEqualTo("$request.body#/user/uuid");
        assertThat(richLinkOperationRef.getDescription()).isEqualTo("description");
        assertThat(richLinkOperationRef.getServer()).isNotNull();
    }

    @Test
    void testEmptyLink(){
        LinkDescriptor emptyLink = getLinkByName("empty_link");
        assertThat(emptyLink.getOperationId()).isNull();
        assertThat(emptyLink.getOperationRef()).isNull();
        assertThat(emptyLink.getParameters()).isEmpty();
        assertThat(emptyLink.getRequestBody()).isNull();
        assertThat(emptyLink.getDescription()).isNull();
        assertThat(emptyLink.getServer()).isNull();
    }

    @Test
    void testEmptyLinkParameters(){
        LinkDescriptor emptyParamsLink = getLinkByName("link_with_empty_parameters");
        assertThat(emptyParamsLink.getParameters()).hasSize(3);
        assertThat(emptyParamsLink.getParameters().get(0).getName()).isNotNull();
        assertThat(emptyParamsLink.getParameters().get(0).getValue()).isNull();
        assertThat(emptyParamsLink.getParameters().get(1).getName()).isNotNull();
        assertThat(emptyParamsLink.getParameters().get(1).getValue()).isNull();
        assertThat(emptyParamsLink.getParameters().get(2).getName()).isNotNull();
        assertThat(emptyParamsLink.getParameters().get(2).getValue()).isNull();
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
        assertThat(responses).hasSize(5);

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

        ResponseDescriptor resNoFields = getResponse("434");
        assertThat(resNoFields.getIsDefault()).isFalse();
        assertThat(resNoFields.getStatusCode()).isEqualTo("434");
        assertThat(resNoFields.getDescription()).isEqualTo("no fields");
        assertThat(resNoFields.getHeaders()).isEmpty();
        assertThat(resNoFields.getMediaTypeObjects()).isEmpty();
        assertThat(resNoFields.getLinks()).isEmpty();
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

    private ResponseDescriptor getResponse(String statusCodeOrDefault){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses) {
            if (response.getIsDefault() && statusCodeOrDefault.equals("default"))
                return response;
            if (!statusCodeOrDefault.equals("default") && statusCodeOrDefault.equals(response.getStatusCode()))
                return response;
        }
        return null;
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

    private LinkDescriptor getLinkByName(String name){
        List<LinkDescriptor> links = contract.getComponents().getLinks();
        for(LinkDescriptor link: links)
            if(link.getName().equals(name))
                return link;
        fail("no link with name <%s> found", name);
        return null;
    }
}
