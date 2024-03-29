package org.jqassistant.plugin.openapi.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Encoding;
import org.jqassistant.plugin.openapi.api.OpenApiScope;
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
        contract = getScanner().scan(file, "/example-components.yaml", OpenApiScope.CONTRACT);

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
        assertThat(requestBodies).hasSize(3);
    }

    @Test
    void testRichRequestBody(){
        RequestBodyDescriptor requestBody = getRequestBodyByName("RichRequestBody");
        assertThat(requestBody.getDescription()).isEqualTo("rich requestBody");
        assertThat(requestBody.getMediaTypes()).hasSize(1);
        assertThat(requestBody.getIsRequired()).isTrue();
    }

    @Test
    void testEmptyRequestBody(){
        RequestBodyDescriptor requestBody = getRequestBodyByName("EmptyRequestBody");
        assertThat(requestBody.getDescription()).isNull();
        assertThat(requestBody.getMediaTypes()).isEmpty();
        assertThat(requestBody.getIsRequired()).isNull();
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

    @Test
    void testSecuritySchemes() {
        List<SecuritySchemeDescriptor> securitySchemes = contract.getComponents().getSecuritySchemes();
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
        assertThat(callbacks).hasSize(2);

        CallbackDescriptor richCallbackPathItem = getCallbackByName("RichCallbackPathItem");
        assertThat(richCallbackPathItem.getRef()).isNull();
        assertThat(richCallbackPathItem.getPathItems()).hasSize(1);

        CallbackDescriptor richCallbackPathRef = getCallbackByName("RichCallbackRef");
        assertThat(richCallbackPathRef.getRef()).isEqualTo("#/components/callbacks/refString");
        assertThat(richCallbackPathRef.getPathItems()).isEmpty();
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

        ResponseDescriptor resDefault = getResponseByStatusCodeOrDefault("default");
        assertThat(resDefault.getIsDefault()).isTrue();
        assertThat(resDefault.getStatusCode()).isNull();
        assertThat(resDefault.getDescription()).isEqualTo("a default response with all fields");
        assertThat(resDefault.getHeaders()).hasSize(1);
        assertThat(resDefault.getMediaTypes()).hasSize(1);
        assertThat(resDefault.getLinks()).hasSize(1);

        ResponseDescriptor resEmpty = getResponseByStatusCodeOrDefault("433");
        assertThat(resEmpty.getIsDefault()).isFalse();
        assertThat(resEmpty.getStatusCode()).isEqualTo("433");
        assertThat(resEmpty.getDescription()).isNull();
        assertThat(resEmpty.getHeaders()).isEmpty();
        assertThat(resEmpty.getMediaTypes()).isEmpty();
        assertThat(resEmpty.getLinks()).isEmpty();

        ResponseDescriptor resNoFields = getResponseByStatusCodeOrDefault("434");
        assertThat(resNoFields.getIsDefault()).isFalse();
        assertThat(resNoFields.getStatusCode()).isEqualTo("434");
        assertThat(resNoFields.getDescription()).isEqualTo("no fields");
        assertThat(resNoFields.getHeaders()).isEmpty();
        assertThat(resNoFields.getMediaTypes()).isEmpty();
        assertThat(resNoFields.getLinks()).isEmpty();
    }

    @Test
    void testMediaType(){
        ResponseDescriptor response404 = getResponseByStatusCodeOrDefault("418");
        assertThat(response404.getMediaTypes()).hasSize(3);

        MediaTypeDescriptor mtoExample = getMediaTypeByName("multipart/form-data_example");
        assertThat(mtoExample.getMediaType()).isEqualTo("multipart/form-data_example");
        assertThat(mtoExample.getSchema()).isNotNull();
        assertThat(mtoExample.getExample()).isNotNull();
        assertThat(mtoExample.getExamples()).isEmpty();
        assertThat(mtoExample.getEncodings()).hasSize(2);

        MediaTypeDescriptor mtoExamples = getMediaTypeByName("multipart/form-data_examples");
        assertThat(mtoExamples.getMediaType()).isEqualTo("multipart/form-data_examples");
        assertThat(mtoExamples.getSchema()).isNotNull();
        assertThat(mtoExamples.getExample()).isNull();
        assertThat(mtoExamples.getExamples()).hasSize(2);
        assertThat(mtoExamples.getEncodings()).isEmpty();

        MediaTypeDescriptor mtoEmpty = getMediaTypeByName("multipart/form-data_empty");
        assertThat(mtoEmpty.getMediaType()).isEqualTo("multipart/form-data_empty");
        assertThat(mtoEmpty.getSchema()).isNull();
        assertThat(mtoEmpty.getExample()).isNull();
        assertThat(mtoEmpty.getExamples()).isEmpty();
        assertThat(mtoEmpty.getEncodings()).isEmpty();
    }

    @Test
    void testEncodings(){
        MediaTypeDescriptor mto = getRequestBodyByName("UserBody").getMediaTypes().get(0);
        assertThat(mto.getEncodings()).hasSize(2);

        EncodingDescriptor encoding1 = getEncodingByPropertyName(mto.getEncodings(), "property1");
        assertThat(encoding1.getPropertyName()).isEqualTo("property1");
        assertThat(encoding1.getContentType()).isEqualTo("application/xml; charset=utf-8");
        assertThat(encoding1.getHeaders()).hasSize(1);
        assertThat(encoding1.getExplode()).isTrue();
        assertThat(encoding1.getAllowsReserved()).isTrue();
        assertThat(encoding1.getStyle()).isEqualTo(Encoding.StyleEnum.valueOf("SPACE_DELIMITED"));

        EncodingDescriptor encoding2 = getEncodingByPropertyName(mto.getEncodings(), "property2");
        assertThat(encoding2.getPropertyName()).isEqualTo("property2");
        assertThat(encoding2.getContentType()).isNull();
        assertThat(encoding2.getHeaders()).isEmpty();
        assertThat(encoding2.getExplode()).isFalse();
        assertThat(encoding2.getAllowsReserved()).isFalse();
        assertThat(encoding2.getStyle()).isEqualTo(Encoding.StyleEnum.valueOf("FORM"));
    }

    @Test
    void testParameterRelation(){
        List<ParameterDescriptor> parameters = contract.getComponents().getParameters();
        assertThat(parameters).hasSize(5);
    }

    @Test
    void testRichParametersWithContent() {
        ParameterDescriptor paramAllFieldsContent = getParamByName("param_all_fields_content");
        assertThat(paramAllFieldsContent.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.HEADER);
        assertThat(paramAllFieldsContent.getDescription()).isEqualTo("token to be passed as a header");
        assertThat(paramAllFieldsContent.getIsRequired()).isTrue();
        assertThat(paramAllFieldsContent.getIsDeprecated()).isTrue();
        assertThat(paramAllFieldsContent.getAllowsEmptyValue()).isNull(); // null when location is HEADER
        assertThat(paramAllFieldsContent.getStyle()).isNull(); // null when content is defined
        assertThat(paramAllFieldsContent.getExplode()).isFalse(); // false when content is defined
        assertThat(paramAllFieldsContent.getAllowsReserved()).isFalse(); // false when content is defined
        assertThat(paramAllFieldsContent.getSchema()).isNull(); // null when content is defined
        assertThat(paramAllFieldsContent.getExample()).isNull(); // null when content is defined
        assertThat(paramAllFieldsContent.getExamples()).isEmpty(); // empty when content is defined
        assertThat(paramAllFieldsContent.getContent()).hasSize(1);
    }

    @Test
    void testRichParametersWithSchema(){
        ParameterDescriptor paramAllFieldsExamples = getParamByName("param_all_fields_examples");
        assertThat(paramAllFieldsExamples.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.QUERY);
        assertThat(paramAllFieldsExamples.getDescription()).isEqualTo("token to be passed as a header");
        assertThat(paramAllFieldsExamples.getIsRequired()).isFalse();
        assertThat(paramAllFieldsExamples.getIsDeprecated()).isFalse();
        assertThat(paramAllFieldsExamples.getAllowsEmptyValue()).isTrue(); // null when location is HEADER
        assertThat(paramAllFieldsExamples.getStyle()).isEqualTo(Parameter.StyleEnum.PIPEDELIMITED);
        assertThat(paramAllFieldsExamples.getExplode()).isTrue();
        assertThat(paramAllFieldsExamples.getAllowsReserved()).isTrue();
        assertThat(paramAllFieldsExamples.getSchema()).isNotNull();
        assertThat(paramAllFieldsExamples.getExample()).isNull();
        assertThat(paramAllFieldsExamples.getExamples()).hasSize(2);
        assertThat(paramAllFieldsExamples.getContent()).isEmpty();

        ParameterDescriptor paramAllFieldsExample = getParamByName("param_all_fields_example");
        assertThat(paramAllFieldsExample.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.QUERY);
        assertThat(paramAllFieldsExample.getDescription()).isEqualTo("token to be passed as a header");
        assertThat(paramAllFieldsExample.getIsRequired()).isFalse();
        assertThat(paramAllFieldsExample.getIsDeprecated()).isFalse();
        assertThat(paramAllFieldsExample.getAllowsEmptyValue()).isTrue(); // null when location is HEADER
        assertThat(paramAllFieldsExample.getStyle()).isEqualTo(Parameter.StyleEnum.PIPEDELIMITED);
        assertThat(paramAllFieldsExample.getExplode()).isTrue();
        assertThat(paramAllFieldsExample.getAllowsReserved()).isTrue();
        assertThat(paramAllFieldsExample.getSchema()).isNotNull();
        assertThat(paramAllFieldsExample.getExample()).isNotNull();
        assertThat(paramAllFieldsExample.getExamples()).isEmpty();
        assertThat(paramAllFieldsExample.getContent()).isEmpty();
    }

    @Test
    void testEmptyParameters(){
        ParameterDescriptor paramEmptyFieldsExamples = getParamByName("param_empty_fields");
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
        assertThat(paramEmptyFieldsExamples.getContent()).isEmpty();

        ParameterDescriptor paramNoFieldsExamples = getParamByName("param_no_fields");
        assertThat(paramNoFieldsExamples.getLocation()).isEqualTo(ParameterDescriptor.ParameterLocation.PATH);
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
        assertThat(paramNoFieldsExamples.getContent()).isEmpty();
    }

    @Test
    void testSchemas() {
        List<SchemaDescriptor> schemas = contract.getComponents().getSchemas();
        assertThat(schemas).hasSize(1);
    }

    private ParameterDescriptor getParamByName(String name) {
        List<ParameterDescriptor> params = contract.getComponents().getParameters();
        for (ParameterDescriptor param : params)
            if (param.getName().equals(name))
                return param;
        return null;
    }

    private ResponseDescriptor getResponseByStatusCodeOrDefault(String statusCodeOrDefault){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses) {
            if (response.getIsDefault() && statusCodeOrDefault.equals("default"))
                return response;
            if (!statusCodeOrDefault.equals("default") && statusCodeOrDefault.equals(response.getStatusCode()))
                return response;
        }
        return null;
    }

    private MediaTypeDescriptor getMediaTypeByName(String name){
        List<ResponseDescriptor> responses = contract.getComponents().getResponses();
        for(ResponseDescriptor response: responses)
            for(MediaTypeDescriptor mediaType: response.getMediaTypes())
                if(mediaType.getMediaType().equals(name))
                    return mediaType;
        return null;
    }

    private RequestBodyDescriptor getRequestBodyByName(String name){
        List<RequestBodyDescriptor> requestBodies = contract.getComponents().getRequestBodies();
        for(RequestBodyDescriptor requestBody: requestBodies)
            if(name.equals(requestBody.getName()))
                return requestBody;
        fail("no requestBody found with name <%s>", name);
        return null;
    }

    private CallbackDescriptor getCallbackByName(String name){
        List<CallbackDescriptor> callbacks = contract.getComponents().getCallBacks();
        for(CallbackDescriptor callback: callbacks)
            if(callback.getName().equals(name))
                return callback;
        fail("No callback with name <%s> found", name);
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

    private EncodingDescriptor getEncodingByPropertyName(List<EncodingDescriptor> encodings, String propertyName){
        for(EncodingDescriptor encoding : encodings)
            if(propertyName.equals(encoding.getPropertyName()))
                return encoding;
        fail("No encoding with propertyName <%s> found.", propertyName);
        return null;
    }

    private HeaderDescriptor getHeaderByName(String name){
        List<HeaderDescriptor> headers = contract.getComponents().getHeaders();
        for (HeaderDescriptor header : headers){
            if (header.getName().equals(name))
                return header;
        }
        return null;
    }
}
