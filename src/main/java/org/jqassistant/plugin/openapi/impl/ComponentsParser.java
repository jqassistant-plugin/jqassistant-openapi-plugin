package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentsParser {

    /**
     * Parses OpenApi Components object to internal object
     *
     * @param components the OpenApi Components object to parse
     * @param store the store object to create internal object from
     * @return parsed internal ComponentsDescriptor object
     */
    public static ComponentsDescriptor parse(Components components, Store store){
        ComponentsDescriptor componentsDescriptor = store.create(ComponentsDescriptor.class);

        if (components.getSchemas() != null)
            componentsDescriptor.getSchemas().addAll(parseSchemas(components.getSchemas(), store));

        if (components.getRequestBodies() != null)
            componentsDescriptor.getRequestBodies().addAll(parseRequestBodies(components.getRequestBodies(), store));

        if (components.getHeaders() != null)
            componentsDescriptor.getHeaders().addAll(parseHeaders(components.getHeaders(), store));

        if (components.getSecuritySchemes() != null)
            componentsDescriptor.getSecuritySchemas().addAll(parseSecuritySchemes(components.getSecuritySchemes(), store));

        if (components.getLinks() != null)
            componentsDescriptor.getLinks().addAll(parseLinks(components.getLinks(), store));

        if (components.getPathItems() != null)
            componentsDescriptor.getPaths().addAll(parsePathItems(components.getPathItems(), store));

        if (components.getCallbacks() != null)
            componentsDescriptor.getCallBacks().addAll(parseCallbacks(components.getCallbacks(), store));

        if (components.getExamples() != null)
            componentsDescriptor.getExamples().addAll(parseExamples(components.getExamples(), store));

        if (components.getResponses() != null)
            componentsDescriptor.getResponses().addAll(parseResponses(components.getResponses(), store));

        if (components.getParameters() != null)
            componentsDescriptor.getParameters().addAll(parseParameters(components.getParameters(), store));

        return componentsDescriptor;
    }

    private static List<SchemaDescriptor> parseSchemas(Map<String, Schema> schemasMap, Store store){
        List<SchemaDescriptor> schemaDescriptors = new ArrayList<>();
        for (Schema<?> schema : schemasMap.values())
            schemaDescriptors.add(Parsers.parseSchema(schema, store));
        return schemaDescriptors;
    }

    private static List<RequestBodyDescriptor> parseRequestBodies(Map<String, RequestBody> requestBodiesMap, Store store){
        List<RequestBodyDescriptor> requestBodyDescriptors = new ArrayList<>();
        for (RequestBody requestBody : requestBodiesMap.values())
            requestBodyDescriptors.add(Parsers.parseRequestBody(requestBody, store));
        return requestBodyDescriptors;
    }

    private static List<HeaderDescriptor> parseHeaders(Map<String, Header> headersMap, Store store){
        List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
        for (Header header : headersMap.values())
            headerDescriptors.add(Parsers.parseHeader(header, store));
        return headerDescriptors;
    }

    private static List<SecuritySchemaDescriptor> parseSecuritySchemes(Map<String, SecurityScheme> securitySchemesMap, Store store){
        List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
        for (SecurityScheme securityScheme : securitySchemesMap.values())
            securitySchemaDescriptors.add(Parsers.parseSecuritySchemas(securityScheme, store));
        return securitySchemaDescriptors;
    }

    private static List<LinkDescriptor> parseLinks(Map<String, Link> linksMap, Store store){
        List<LinkDescriptor> linkDescriptors = new ArrayList<>();
        for (Link link : linksMap.values())
            linkDescriptors.add(Parsers.parseLink(link, store));
        return linkDescriptors;
    }

    private static List<PathDescriptor> parsePathItems(Map<String, PathItem> pathItemsMap, Store store){
        List<PathDescriptor> pathDescriptors = new ArrayList<>();
        for(String pathUrl: pathItemsMap.keySet())
            pathDescriptors.add(Parsers.parsePath(pathUrl, pathItemsMap.get(pathUrl), store));
        return pathDescriptors;
    }

    private static List<CallbackDescriptor> parseCallbacks(Map<String, Callback> callbacksMap, Store store){
        List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
        for(Callback callback : callbacksMap.values())
            callbackDescriptors.add(Parsers.parseCallback(callback, store));
        return callbackDescriptors;
    }

    private static List<ExampleDescriptor> parseExamples(Map<String, Example> examplesMap, Store store){
        List<ExampleDescriptor> exampleDescriptors = new ArrayList<>();
        for(Example example : examplesMap.values())
            exampleDescriptors.add(Parsers.parseExample(example, store));
        return exampleDescriptors;
    }

    private static List<ResponseDescriptor> parseResponses(Map<String, ApiResponse> apiResponsesMap, Store store){
        List<ResponseDescriptor> responseDescriptors = new ArrayList<>();
        for(String statusCodeOrDefault : apiResponsesMap.keySet())
            responseDescriptors.add(Parsers.parseResponse(statusCodeOrDefault, apiResponsesMap.get(statusCodeOrDefault), store));
        return responseDescriptors;
    }

    private static List<ParameterDescriptor> parseParameters(Map<String, Parameter> parametersMap, Store store){
        List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
        for (Parameter parameter : parametersMap.values())
            parameterDescriptors.add(Parsers.parseParameter(parameter, store));
        return parameterDescriptors;
    }
}