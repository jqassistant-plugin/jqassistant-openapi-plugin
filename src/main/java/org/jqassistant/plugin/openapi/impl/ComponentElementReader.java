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

import java.util.ArrayList;
import java.util.List;

public class ComponentElementReader {
    private final OpenAPIScannerPlugin openAPIScannerPlugin;

    public ComponentElementReader(OpenAPIScannerPlugin openAPIScannerPlugin) {
        this.openAPIScannerPlugin = openAPIScannerPlugin;
    }


    public void readParameters(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
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

    public void readResponses(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getResponses() != null && !components.getResponses().isEmpty()) {
            ApiResponses apiResponses = new ApiResponses();
            apiResponses.putAll(components.getResponses());
            List<ResponseDescriptor> responseDescriptors = openAPIScannerPlugin.parseResponses(apiResponses, store);
            componentsDescriptor.getResponses().addAll(responseDescriptors);
        }
    }

    public void readExamples(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getExamples() != null && !components.getExamples().isEmpty()) {
            List<ExampleDescriptor> exampleDescriptors = new ArrayList<>();
            for (Example example : components.getExamples().values()) {
                exampleDescriptors.add(openAPIScannerPlugin.parseExamples(example, store));
            }
            componentsDescriptor.getExamples().addAll(exampleDescriptors);
        }
    }

    public void readCallbacks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getCallbacks() != null && !components.getCallbacks().isEmpty()) {
            List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
            for (Callback callback : components.getCallbacks().values()) {
                callbackDescriptors.add(openAPIScannerPlugin.parseCallbacks(callback, store));
            }
            componentsDescriptor.getCallBacks().addAll(callbackDescriptors);
        }
    }

    public void readLinks(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getLinks() != null && !components.getLinks().isEmpty()) {
            List<LinkDescriptor> linkDescriptors = new ArrayList<>();
            for (Link link : components.getLinks().values()) {
                linkDescriptors.add(openAPIScannerPlugin.parseLinks(link, store));
            }
            componentsDescriptor.getLinks().addAll(linkDescriptors);
        }
    }

    public void readSecuritySchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getSecuritySchemes() != null && !components.getSecuritySchemes().isEmpty()) {
            List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
            for (SecurityScheme securityScheme : components.getSecuritySchemes().values()) {
                securitySchemaDescriptors.add(openAPIScannerPlugin.parseSecuritySchemas(securityScheme, store));
            }
            componentsDescriptor.getSecuritySchemas().addAll(securitySchemaDescriptors);
        }
    }

    public void readHeaders(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getHeaders() != null && !components.getHeaders().isEmpty()) {
            List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
            for (Header header : components.getHeaders().values()) {
                headerDescriptors.add(openAPIScannerPlugin.parseHeaders(header, store));
            }
            componentsDescriptor.getHeaders().addAll(headerDescriptors);
        }
    }

    public void readPathItems(Components components, Store store, ComponentsDescriptor componentsDescriptor){
        if (components.getPathItems() != null && !components.getPathItems().isEmpty()) {
            Paths paths = new Paths();
            paths.putAll(components.getPathItems());
            componentsDescriptor.getPaths().addAll(openAPIScannerPlugin.parsePaths(paths, store));
        }
    }

    public void readRequestBodies(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getRequestBodies() != null && !components.getRequestBodies().isEmpty()) {
            List<RequestBodyDescriptor> requestBodyDescriptors = new ArrayList<>();
            for (RequestBody requestBody : components.getRequestBodies().values()) {
                requestBodyDescriptors.add(openAPIScannerPlugin.parseRequestBody(requestBody, store));
            }
            componentsDescriptor.getRequestBodies().addAll(requestBodyDescriptors);
        }
    }

    public void readSchemas(Components components, Store store, ComponentsDescriptor componentsDescriptor) {
        if (components.getSchemas() != null && !components.getSchemas().isEmpty()) {
            List<SchemaDescriptor> schemaDescriptors = new ArrayList<>();
            for (Schema schema : components.getSchemas().values()) {
                schemaDescriptors.add(openAPIScannerPlugin.parseSchema(schema, store));
            }
            componentsDescriptor.getSchemas().addAll(schemaDescriptors);
        }
    }

}
