package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentsParser {

    private ComponentsParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static ComponentsDescriptor parse(Components components, Store store){
        ComponentsDescriptor componentsDescriptor = store.create(ComponentsDescriptor.class);

        if (components.getSchemas() != null)
            componentsDescriptor.getSchemas().addAll(parseSchemas(components.getSchemas(), store));

        if (components.getResponses() != null)
            componentsDescriptor.getResponses().addAll(ResponseParser.parseAll(components.getResponses(), store));

        if (components.getParameters() != null)
            componentsDescriptor.getParameters().addAll(ParameterParser.parseAll(components.getParameters(), store));

        if (components.getExamples() != null)
            componentsDescriptor.getExamples().addAll(ExampleParser.parseAll(components.getExamples(), store));

        if (components.getRequestBodies() != null)
            componentsDescriptor.getRequestBodies().addAll(RequestBodyParser.parseAll(components.getRequestBodies(), store));

        if (components.getHeaders() != null)
            componentsDescriptor.getHeaders().addAll(parseHeaders(components.getHeaders(), store));

        if (components.getSecuritySchemes() != null)
            componentsDescriptor.getSecuritySchemas().addAll(parseSecuritySchemes(components.getSecuritySchemes(), store));

        if (components.getLinks() != null)
            componentsDescriptor.getLinks().addAll(parseLinks(components.getLinks(), store));

        if (components.getCallbacks() != null)
            componentsDescriptor.getCallBacks().addAll(parseCallbacks(components.getCallbacks(), store));

        return componentsDescriptor;
    }

    // schema map only occurs as child of components
    private static List<SchemaDescriptor> parseSchemas(Map<String, Schema> schemasMap, Store store){
        List<SchemaDescriptor> schemaDescriptors = new ArrayList<>();
        schemasMap.forEach((s, schema) -> schemaDescriptors.add(Parsers.parseSchema(schema, store)));
        return schemaDescriptors;
    }

    // secSchema map only occurs as child of components
    private static List<SecuritySchemaDescriptor> parseSecuritySchemes(Map<String, SecurityScheme> securitySchemesMap, Store store){
        List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
        securitySchemesMap.forEach((s, securityScheme) -> securitySchemaDescriptors.add(Parsers.parseSecurityScheme(securityScheme, store)));
        return securitySchemaDescriptors;
    }

    // TODO make func parseHeaders reusable
    // gets used by parents components, encoding, response
    private static List<HeaderDescriptor> parseHeaders(Map<String, Header> headersMap, Store store){
        List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
        headersMap.forEach((s, header) -> headerDescriptors.add(Parsers.parseHeader(header, store)));
        return headerDescriptors;
    }

    // TODO make func parseLinks reusable
    // gets used by parents components, response,
    private static List<LinkDescriptor> parseLinks(Map<String, Link> linksMap, Store store){
        List<LinkDescriptor> linkDescriptors = new ArrayList<>();
        linksMap.forEach((s, link) -> linkDescriptors.add(Parsers.parseLink(link, store)));
        return linkDescriptors;
    }

    // TODO make func parseCallbacks reusable
    // gets used by parents components, operation,
    private static List<CallbackDescriptor> parseCallbacks(Map<String, Callback> callbacksMap, Store store){
        List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
        callbacksMap.forEach((s, callback) -> callbackDescriptors.add(Parsers.parseCallback(callback, store)));
        return callbackDescriptors;
    }

}
