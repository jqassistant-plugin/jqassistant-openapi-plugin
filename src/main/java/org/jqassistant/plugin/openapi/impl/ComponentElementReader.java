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
    private final OpenAPIScannerPlugin openAPIScannerPlugin; // TODO needed?

    public ComponentElementReader(OpenAPIScannerPlugin openAPIScannerPlugin) {
        this.openAPIScannerPlugin = openAPIScannerPlugin;
    }


    /**
     * Parses OpenApi Components object to internal object
     *
     * @param components the OpenApi Components object to parse
     * @param store the store object to create internal object from
     * @return parsed internal ComponentsDescriptor object
     */
    public ComponentsDescriptor parseComponents(Components components, Store store) {
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


    void readParameters(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getParameters() != null && !components.getParameters().isEmpty()) {
            List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
            for (Parameter parameter : components.getParameters().values()) {
                List<Parameter> singleParameterList = new ArrayList<>();
                singleParameterList.add(parameter);
                parameterDescriptors.addAll(openAPIScannerPlugin.parseParameters(singleParameterList, store));
            }

            componentsDescriptor.getParameters().addAll(parameterDescriptors);
        }
    }

    void readResponses(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getResponses() != null && !components.getResponses().isEmpty()) {
            ApiResponses apiResponses = new ApiResponses();
            apiResponses.putAll(components.getResponses());
            List<ResponseDescriptor> responseDescriptors = openAPIScannerPlugin.parseResponses(apiResponses, store);
            componentsDescriptor.getResponses().addAll(responseDescriptors);
        }
    }

    void readExamples(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getExamples() != null && !components.getExamples().isEmpty()) {
            List<ExampleDescriptor> exampleDescriptors = new ArrayList<>();
            for (Example example : components.getExamples().values()) {
                exampleDescriptors.add(openAPIScannerPlugin.parseExamples(example, store));
            }
            componentsDescriptor.getExamples().addAll(exampleDescriptors);
        }
    }

    void readCallbacks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getCallbacks() != null && !components.getCallbacks().isEmpty()) {
            List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
            for (Callback callback : components.getCallbacks().values()) {
                callbackDescriptors.add(openAPIScannerPlugin.parseCallbacks(callback, store));
            }
            componentsDescriptor.getCallBacks().addAll(callbackDescriptors);
        }
    }

    void readLinks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getLinks() != null && !components.getLinks().isEmpty()) {
            List<LinkDescriptor> linkDescriptors = new ArrayList<>();
            for (Link link : components.getLinks().values()) {
                linkDescriptors.add(openAPIScannerPlugin.parseLinks(link, store));
            }
            componentsDescriptor.getLinks().addAll(linkDescriptors);
        }
    }

    void readSecuritySchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getSecuritySchemes() != null && !components.getSecuritySchemes().isEmpty()) {
            List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
            for (SecurityScheme securityScheme : components.getSecuritySchemes().values()) {
                securitySchemaDescriptors.add(openAPIScannerPlugin.parseSecuritySchemas(securityScheme, store));
            }
            componentsDescriptor.getSecuritySchemas().addAll(securitySchemaDescriptors);
        }
    }

    void readHeaders(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getHeaders() != null && !components.getHeaders().isEmpty()) {
            List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
            for (Header header : components.getHeaders().values()) {
                headerDescriptors.add(openAPIScannerPlugin.parseHeaders(header, store));
            }
            componentsDescriptor.getHeaders().addAll(headerDescriptors);
        }
    }

    void readPathItems(Components components, Store store, ComponentsDescriptor componentsDescriptor){
        if (components.getPathItems() != null && !components.getPathItems().isEmpty()) {
            Paths paths = new Paths();
            paths.putAll(components.getPathItems());
            componentsDescriptor.getPaths().addAll(openAPIScannerPlugin.parsePaths(paths, store));
        }
    }

    void readRequestBodies(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getRequestBodies() != null && !components.getRequestBodies().isEmpty()) {
            List<RequestBodyDescriptor> requestBodyDescriptors = new ArrayList<>();
            for (RequestBody requestBody : components.getRequestBodies().values()) {
                requestBodyDescriptors.add(openAPIScannerPlugin.parseRequestBody(requestBody, store));
            }
            componentsDescriptor.getRequestBodies().addAll(requestBodyDescriptors);
        }
    }

    void readSchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
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
    org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor parseSchema(String name, Schema<?> schema, Store store) {
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
}
