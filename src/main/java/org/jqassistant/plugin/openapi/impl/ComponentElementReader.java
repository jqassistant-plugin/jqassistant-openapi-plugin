package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.*;
import org.jqassistant.plugin.openapi.api.model.jsonschema.ObjectPropertyDescriptor;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;
import org.jqassistant.plugin.openapi.impl.jsonschema.JSONSchemaObjectReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ComponentElementReader {
    /**
     * Parses OpenApi Components object to internal object
     *
     * @param components the OpenApi Components object to parse
     * @param store the store object to create internal object from
     * @return parsed internal ComponentsDescriptor object
     */
    static public ComponentsDescriptor parseComponents(Components components, Store store) {
        ComponentsDescriptor componentsDescriptor = store.create(ComponentsDescriptor.class);

        readSchemas(components, store, componentsDescriptor);
        readRequestBodies(components, store, componentsDescriptor);
        readHeaders(components, store, componentsDescriptor);
        readSecuritySchemas(components, store, componentsDescriptor);
        readLinks(components, store, componentsDescriptor);
        readPathItems(components, store, componentsDescriptor);
        readCallbacks(components, store, componentsDescriptor);
        readExamples(components, store, componentsDescriptor);
        readResponses(components, store, componentsDescriptor);
        readParameters(components, store, componentsDescriptor);

        return componentsDescriptor;
    }


    static void readParameters(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getParameters() != null && !components.getParameters().isEmpty()) {
            List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
            for (Parameter parameter : components.getParameters().values()) {
                List<Parameter> singleParameterList = new ArrayList<>();
                singleParameterList.add(parameter);
                parameterDescriptors.addAll(OpenAPIScannerPlugin.parseParameters(singleParameterList, store));
            }

            componentsDescriptor.getParameters().addAll(parameterDescriptors);
        }
    }

    static void readResponses(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getResponses() != null && !components.getResponses().isEmpty()) {
            ApiResponses apiResponses = new ApiResponses();
            apiResponses.putAll(components.getResponses());
            List<ResponseDescriptor> responseDescriptors = OpenAPIScannerPlugin.parseResponses(apiResponses, store);
            componentsDescriptor.getResponses().addAll(responseDescriptors);
        }
    }

    static void readExamples(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getExamples() != null && !components.getExamples().isEmpty()) {
            List<ExampleDescriptor> exampleDescriptors = new ArrayList<>();
            for (Example example : components.getExamples().values()) {
                exampleDescriptors.add(parseExamples(example, store));
            }
            componentsDescriptor.getExamples().addAll(exampleDescriptors);
        }
    }

    static void readCallbacks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getCallbacks() != null && !components.getCallbacks().isEmpty()) {
            List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
            for (Callback callback : components.getCallbacks().values()) {
                callbackDescriptors.add(parseCallbacks(callback, store));
            }
            componentsDescriptor.getCallBacks().addAll(callbackDescriptors);
        }
    }

    static void readLinks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getLinks() != null && !components.getLinks().isEmpty()) {
            List<LinkDescriptor> linkDescriptors = new ArrayList<>();
            for (Link link : components.getLinks().values()) {
                linkDescriptors.add(parseLinks(link, store));
            }
            componentsDescriptor.getLinks().addAll(linkDescriptors);
        }
    }

    static void readSecuritySchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getSecuritySchemes() != null && !components.getSecuritySchemes().isEmpty()) {
            List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
            for (SecurityScheme securityScheme : components.getSecuritySchemes().values()) {
                securitySchemaDescriptors.add(parseSecuritySchemas(securityScheme, store));
            }
            componentsDescriptor.getSecuritySchemas().addAll(securitySchemaDescriptors);
        }
    }

    static void readHeaders(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getHeaders() != null && !components.getHeaders().isEmpty()) {
            List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
            for (Header header : components.getHeaders().values()) {
                headerDescriptors.add(parseHeaders(header, store));
            }
            componentsDescriptor.getHeaders().addAll(headerDescriptors);
        }
    }

    static void readPathItems(Components components, Store store, ComponentsDescriptor componentsDescriptor){
        if (components.getPathItems() != null && !components.getPathItems().isEmpty()) {
            Paths paths = new Paths();
            paths.putAll(components.getPathItems());
            componentsDescriptor.getPaths().addAll(OpenAPIScannerPlugin.parsePaths(paths, store));
        }
    }

    static void readRequestBodies(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getRequestBodies() != null && !components.getRequestBodies().isEmpty()) {
            List<RequestBodyDescriptor> requestBodyDescriptors = new ArrayList<>();
            for (RequestBody requestBody : components.getRequestBodies().values()) {
                requestBodyDescriptors.add(OpenAPIScannerPlugin.parseRequestBody(requestBody, store));
            }
            componentsDescriptor.getRequestBodies().addAll(requestBodyDescriptors);
        }
    }

    static void readSchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getSchemas() != null && !components.getSchemas().isEmpty()) {

            List<org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor> schemaDescriptors = new ArrayList<>();

            for (String name : components.getSchemas().keySet()){
                schemaDescriptors.add(parseSchema(name, components.getSchemas().get(name), store));
            }

            componentsDescriptor.getSchemas().addAll(schemaDescriptors);
        }
    }


    /**
     *
     Parses an OpenAPI Schema object and creates a SchemaDescriptor based on the provided Schema and Store.
     @param schema The Schema object to parse.
     @param store The Store object used to create the SchemaDescriptor.
     @return The parsed SchemaDescriptor object.
     */
    static org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor parseSchema(String name, Schema<?> schema, Store store) {
        org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);

        JSONSchemaObjectReader or = new JSONSchemaObjectReader(store);

        if (Objects.equals(schema.getType(), ObjectPropertyDescriptor.TYPE_NAME)){
            schemaDescriptor.setObject(or.parseObject(name, schema));
        } else {
            throw new RuntimeException("Unknown schema!");
        }


        schemaDescriptor.setName(name);
        return schemaDescriptor;
    }

    /**
     Parses a Callback object and creates a CallbackDescriptor based on the provided Callback and Store.
     *
     @param callback The Callback object to parse.
     @param store The Store object used to create the CallbackDescriptor.
     @return The parsed CallbackDescriptor object.
     */
    static CallbackDescriptor parseCallbacks(Callback callback, Store store){
        CallbackDescriptor callbackDescriptor = store.create(CallbackDescriptor.class);

        if(callback.get$ref() != null){
            callbackDescriptor.setRef(callback.get$ref());
        }

        return callbackDescriptor;
    }


    /**
     Parses a SecurityScheme object and creates a SecuritySchemaDescriptor based on the provided SecurityScheme and Store.
     @param securityScheme The SecurityScheme object to parse.
     @param store The Store object used to create the SecuritySchemaDescriptor.
     @return The parsed SecuritySchemaDescriptor object.
     */
    static SecuritySchemaDescriptor parseSecuritySchemas(SecurityScheme securityScheme, Store store){
        SecuritySchemaDescriptor securitySchemaDescriptor = store.create(SecuritySchemaDescriptor.class);

        if(securityScheme.getName() != null){
            securitySchemaDescriptor.setName(securityScheme.getName());
        }

        return securitySchemaDescriptor;
    }

    /**
     Parses a Link object and creates a LinkDescriptor based on the provided Link and Store.
     @param link The Link object to parse.
     @param store The Store object used to create the LinkDescriptor.
     @return The parsed LinkDescriptor object.
     */
    static LinkDescriptor parseLinks(Link link, Store store){
        LinkDescriptor linkDescriptor = store.create(LinkDescriptor.class);

        if(linkDescriptor.getOperationRef() != null){
            linkDescriptor.setOperationRef(link.getOperationRef());
        }

        return linkDescriptor;
    }

    /**

     Parses a Header object and creates a HeaderDescriptor based on the provided Header and Store.
     @param header The Header object to parse.
     @param store The Store object used to create the HeaderDescriptor.
     @return The parsed HeaderDescriptor object.
     */
    static HeaderDescriptor parseHeaders(Header header, Store store){
        HeaderDescriptor headerDescriptor = store.create(HeaderDescriptor.class);

        if(headerDescriptor.getDescription() != null){
            headerDescriptor.setDescription(header.getDescription());
        }

        return headerDescriptor;
    }

    /**

     Parses an Example object and creates an ExampleDescriptor based on the provided Example and Store.
     @param example The Example object to parse.
     @param store The Store object used to create the ExampleDescriptor.
     @return The parsed ExampleDescriptor object.
     */
    static ExampleDescriptor parseExamples(Example example, Store store){
        ExampleDescriptor exampleDescriptor = store.create(ExampleDescriptor.class);

        if(exampleDescriptor.getDescription() != null){
            exampleDescriptor.setDescription(example.getDescription());
        }

        return exampleDescriptor;
    }
}
