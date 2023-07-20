package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.jqassistant.plugin.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

    /**
     * Parses OpenAPI info object to internal properties
     * @param info object to parse
     * @param contractDescriptor object on which properties get set
     * @param store store object to create internal object from
     */
    static void parseInfo(Info info, ContractDescriptor contractDescriptor, Store store){
        if(info.getTitle() != null)
            contractDescriptor.setTitle(info.getTitle());
        if (info.getDescription() != null)
            contractDescriptor.setDescription(info.getDescription());
        if (info.getVersion() != null)
            contractDescriptor.setApiVersion(info.getVersion());
        if (info.getContact() != null)
            contractDescriptor.setContact(Parsers.parseContact(info.getContact(), store));
    }

    /**
     * Parses OpenApi requestBody object to internal object
     *
     * @param requestBody object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    static RequestBodyDescriptor parseRequestBody(RequestBody requestBody, Store store){
        RequestBodyDescriptor requestBodyDescriptor = store.create(RequestBodyDescriptor.class);

        // read properties
        if(requestBody.getDescription() != null && !requestBody.getDescription().isEmpty())
            requestBodyDescriptor.setDescription(requestBody.getDescription());
        if(requestBody.getRequired() != null)
            requestBodyDescriptor.setIsRequired(requestBody.getRequired());

        // read content
        if(requestBody.getContent() != null && !requestBody.getContent().isEmpty())
            requestBodyDescriptor.getMediaTypeObjects().addAll(parseContent(requestBody.getContent(), store));

        return requestBodyDescriptor;
    }

    /**
     * Parses OpenApi Content object
     *
     * @param content object to parse
     * @param store store object to create internal object from
     * @return list of parsed internal mediaType objects
     */
    static List<MediaTypeObjectDescriptor> parseContent(Content content, Store store){
        List<MediaTypeObjectDescriptor> retMediaTypeObjects = new ArrayList<>();
        content.forEach((mediaType, mediaTypeObject) -> retMediaTypeObjects.add(parseMediaTypeObject(mediaType, mediaTypeObject, store)));
        return retMediaTypeObjects;
    }

    /**
     * Parses OpenApi mediaTypeObject object to internal object
     *
     * @param mediaType media type or media type range
     * @param mediaTypeObject object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    static MediaTypeObjectDescriptor parseMediaTypeObject(String mediaType, MediaType mediaTypeObject, Store store){
        // TODO implement examples
        // TODO implement encoding
        // TODO implement schemas

        MediaTypeObjectDescriptor mediaTypeObjectDescriptor = store.create(MediaTypeObjectDescriptor.class);

        // read properties
        mediaTypeObjectDescriptor.setMediaType(mediaType);

        return mediaTypeObjectDescriptor;
    }

    /**
     * Parses list of OpenApi Path objects
     *
     * @param paths list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    static List<PathDescriptor> parsePaths (Paths paths, Store store){
        List<PathDescriptor> pathDescriptors = new ArrayList<>();
        paths.forEach((pathname, pathItem) -> pathDescriptors.add(parsePath(pathname, pathItem, store)));
        return pathDescriptors;
    }

    /**
     * Parses OpenApi Path object to internal object
     *
     * @param pathUrl url of the path to parse
     * @param pathItem object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    static PathDescriptor parsePath (String pathUrl, PathItem pathItem, Store store){
        PathDescriptor pathDescriptor = store.create(PathDescriptor.class);

        pathDescriptor.setPathUrl(pathUrl);

        if(pathItem.get$ref() != null && !pathItem.get$ref().isEmpty())
            pathDescriptor.set$ref(pathItem.get$ref());
        if(pathItem.getSummary() != null && !pathItem.getSummary().isEmpty())
            pathDescriptor.setSummary(pathItem.getSummary());
        if(pathItem.getDescription() != null && !pathItem.getDescription().isEmpty())
            pathDescriptor.setDescription(pathItem.getDescription());

        // Read all Servers
        if(pathItem.getServers() != null && !pathItem.getServers().isEmpty())
            pathDescriptor.getServers().addAll(parseSevers(pathItem.getServers(), store));

        // Read all Parameters
        if(pathItem.getParameters() != null && !pathItem.getParameters().isEmpty())
            pathDescriptor.getParameters().addAll(parseParameters(pathItem.getParameters(), store));

        // Read all Operations
        List<OperationDescriptor> pathOperations = pathDescriptor.getOperations();
        if(pathItem.getGet() != null)
            pathOperations.add(parseOperation(pathItem.getGet(), OperationDescriptor.HTTPMethod.GET, store));
        if(pathItem.getPut() != null)
            pathOperations.add(parseOperation(pathItem.getPut(), OperationDescriptor.HTTPMethod.PUT, store));
        if(pathItem.getPost() != null)
            pathOperations.add(parseOperation(pathItem.getPost(), OperationDescriptor.HTTPMethod.POST, store));
        if(pathItem.getDelete() != null)
            pathOperations.add(parseOperation(pathItem.getDelete(), OperationDescriptor.HTTPMethod.DELETE, store));
        if(pathItem.getOptions() != null)
            pathOperations.add(parseOperation(pathItem.getOptions(), OperationDescriptor.HTTPMethod.OPTIONS, store));
        if(pathItem.getHead() != null)
            pathOperations.add(parseOperation(pathItem.getHead(), OperationDescriptor.HTTPMethod.HEAD, store));
        if(pathItem.getPatch() != null)
            pathOperations.add(parseOperation(pathItem.getPatch(), OperationDescriptor.HTTPMethod.PATCH, store));
        if(pathItem.getTrace() != null)
            pathOperations.add(parseOperation(pathItem.getTrace(), OperationDescriptor.HTTPMethod.TRACE, store));

        return pathDescriptor;
    }

    /**
     * Parses OpenApi operation object to internal object
     *
     * @param httpMethod http method of the operation to parse
     * @param operation object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    static OperationDescriptor parseOperation(Operation operation, OperationDescriptor.HTTPMethod httpMethod, Store store){
        // TODO tags
        // TODO externalDocs
        // TODO security

        OperationDescriptor operationDescriptor = store.create(OperationDescriptor.class);

        // read properties
        operationDescriptor.setType(httpMethod);
        if(operation.getSummary() != null && !operation.getSummary().isEmpty())
            operationDescriptor.setSummary(operation.getSummary());
        if(operation.getDescription() != null && !operation.getDescription().isEmpty())
            operationDescriptor.setDescription(operation.getDescription());
        if(operation.getOperationId() != null && !operation.getOperationId().isEmpty())
            operationDescriptor.setOperationId(operation.getOperationId());
        if(operation.getDeprecated() != null)
            operationDescriptor.setIsDeprecated(operation.getDeprecated());
        else
            operationDescriptor.setIsDeprecated(false); // Default Value

        // read responses
        if(operation.getResponses() != null && !operation.getResponses().isEmpty())
            operationDescriptor.getResponses().addAll(parseResponses(operation.getResponses(), store));

        // read requestBody
        if(operation.getRequestBody() != null)
            operationDescriptor.setRequestBody(parseRequestBody(operation.getRequestBody(), store));

        return operationDescriptor;
    }

    /**
     * Parses list of OpenApi Response objects
     *
     * @param responses list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    static List<ResponseDescriptor> parseResponses(ApiResponses responses, Store store){
        List<ResponseDescriptor> retResponses = new ArrayList<>();
        responses.forEach((s, apiResponse) -> retResponses.add(parseResponse(s, apiResponse, store)));
        return retResponses;
    }

    /**
     * Parses OpenApi response object to internal object
     *
     * @param statusCodeOrDefault statusCode of response or "default"
     * @param response object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    static ResponseDescriptor parseResponse(String statusCodeOrDefault, ApiResponse response, Store store){
        // TODO implement mediaTypeObject parsing

        ResponseDescriptor responseDescriptor = store.create(ResponseDescriptor.class);

        // read statusCode and default flag
        if(statusCodeOrDefault.equals("default"))
            responseDescriptor.setIsDefault(true);
        else {
            responseDescriptor.setIsDefault(false);
            responseDescriptor.setStatusCode(statusCodeOrDefault);
        }

        // read properties
        if(response.getDescription() != null && !response.getDescription().isEmpty())
            responseDescriptor.setDescription(response.getDescription());

        // read content
        if(response.getContent() != null && !response.getContent().isEmpty())
            responseDescriptor.getMediaTypeObject().addAll(parseContent(response.getContent(), store));

        return responseDescriptor;
    }

    /**
     * Parses list of OpenApi Parameter objects
     *
     * @param parameters list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    static List<ParameterDescriptor> parseParameters(List<Parameter> parameters, Store store){
        // TODO implement scanning of further props (might also be still missing in param descriptor)

        List<ParameterDescriptor> retParameters = new ArrayList<>();

        for (Parameter parameter : parameters){
            ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);

            // read properties
            if(parameter.getName() != null && !parameter.getName().isEmpty())
                parameterDescriptor.setName(parameter.getName());
            if(parameter.getIn() != null && !parameter.getIn().isEmpty())
                parameterDescriptor.setLocation(ParameterDescriptor.ParameterLocation
                        .valueOf(parameter.getIn().toUpperCase()));
            if(parameter.getDescription() != null && !parameter.getDescription().isEmpty())
                parameterDescriptor.setDescription(parameter.getDescription());
            if(parameter.getRequired() != null)
                parameterDescriptor.setIsRequired(parameter.getRequired());
            if(parameter.getDeprecated() != null)
                parameterDescriptor.setIsDeprecated(parameter.getDeprecated());
            if(parameter.getAllowEmptyValue() != null)
                parameterDescriptor.setAllowsEmptyValue(parameter.getAllowEmptyValue());

            retParameters.add(parameterDescriptor);
        }

        return retParameters;
    }

    /**
     * Parses OpenApi Contact object to Internal Object
     *
     * @param contact Object to parse
     * @param store Store object to create Internal Object from
     * @return parsed internal Object
     */
    static ContactDescriptor parseContact(Contact contact, Store store){
        ContactDescriptor contactDescriptor = store.create(ContactDescriptor.class);

        if(contact.getName() != null)
            contactDescriptor.setName(contact.getName());
        if(contact.getEmail() != null)
            contactDescriptor.setEmail(contact.getEmail());
        if(contact.getUrl() != null)
            contactDescriptor.setUrl(contact.getUrl());

        return contactDescriptor;
    }

    /**
     * Parses OpenApi Tag List to Internal Object list
     *
     * @param tags Object to parse
     * @param store Store object to create Internal Object from
     * @return parsed internal Object
     */
    static List<TagDescriptor> parseTags(List<Tag> tags, Store store){
        ArrayList<TagDescriptor> retTags = new ArrayList<>();

        for (Tag tag : tags){
            TagDescriptor tagDescriptor = store.create(TagDescriptor.class);
            if(tag.getName() != null)
                tagDescriptor.setTag(tag.getName());
            if(tag.getDescription() != null)
                tagDescriptor.setDescription(tag.getDescription());

            retTags.add(tagDescriptor);
        }

        return retTags;
    }

    /**
     * Parses OpenApi Server List to Internal Object list
     *
     * @param servers Object to parse
     * @param store Store object to create Internal Object from
     * @return parsed internal Object
     */
    static List<ServerDescriptor> parseSevers(List<Server> servers, Store store){
        ArrayList<ServerDescriptor> retServers = new ArrayList<>();

        for (Server server : servers){
            ServerDescriptor serverDescriptor = store.create(ServerDescriptor.class);

            serverDescriptor.setUrl(server.getUrl());

            if(server.getDescription() != null)
                serverDescriptor.setDescription(server.getDescription());

            retServers.add(serverDescriptor);
        }

        return retServers;
    }

    /**
     Parses a Callback object and creates a CallbackDescriptor based on the provided Callback and Store.
     *
     @param callback The Callback object to parse.
     @param store The Store object used to create the CallbackDescriptor.
     @return The parsed CallbackDescriptor object.
     */
    static CallbackDescriptor parseCallback(Callback callback, Store store){
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
    static ExampleDescriptor parseExample(Example example, Store store){
        ExampleDescriptor exampleDescriptor = store.create(ExampleDescriptor.class);

        if(exampleDescriptor.getDescription() != null){
            exampleDescriptor.setDescription(example.getDescription());
        }

        return exampleDescriptor;
    }

    /**
     *
     Parses an OpenAPI Schema object and creates a SchemaDescriptor based on the provided Schema and Store.
     @param schema The Schema object to parse.
     @param store The Store object used to create the SchemaDescriptor.
     @return The parsed SchemaDescriptor object.
     */
    static SchemaDescriptor parseSchema(Schema<?> schema, Store store){
        SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);

        if(schemaDescriptor.getName() != null){
            schemaDescriptor.setName(schema.getName());
        }

        return schemaDescriptor;
    }

    /**
     * Parses OpenApi Components object to internal object
     *
     * @param components the OpenApi Components object to parse
     * @param store the store object to create internal object from
     * @return parsed internal ComponentsDescriptor object
     */
    static ComponentsDescriptor parseComponents(Components components, Store store) {
        ComponentsDescriptor componentsDescriptor = store.create(ComponentsDescriptor.class);

        //componentElementReader.readSchemas(components, store, componentsDescriptor);
        if (components.getSchemas() != null && !components.getSchemas().isEmpty()) {
            List<SchemaDescriptor> schemaDescriptors = new ArrayList<>();
            for (Schema<?> schema : components.getSchemas().values()) {
                schemaDescriptors.add(parseSchema(schema, store));
            }
            componentsDescriptor.getSchemas().addAll(schemaDescriptors);
        }

        //componentElementReader.readRequestBodies(components, store, componentsDescriptor);
        if (components.getRequestBodies() != null && !components.getRequestBodies().isEmpty()) {
            List<RequestBodyDescriptor> requestBodyDescriptors = new ArrayList<>();
            for (RequestBody requestBody : components.getRequestBodies().values()) {
                requestBodyDescriptors.add(parseRequestBody(requestBody, store));
            }
            componentsDescriptor.getRequestBodies().addAll(requestBodyDescriptors);
        }

        //componentElementReader.readHeaders(components, store, componentsDescriptor);
        if (components.getHeaders() != null && !components.getHeaders().isEmpty()) {
            List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
            for (Header header : components.getHeaders().values()) {
                headerDescriptors.add(parseHeaders(header, store));
            }
            componentsDescriptor.getHeaders().addAll(headerDescriptors);
        }

        //componentElementReader.readSecuritySchemas(components, store, componentsDescriptor);
        if (components.getSecuritySchemes() != null && !components.getSecuritySchemes().isEmpty()) {
            List<SecuritySchemaDescriptor> securitySchemaDescriptors = new ArrayList<>();
            for (SecurityScheme securityScheme : components.getSecuritySchemes().values()) {
                securitySchemaDescriptors.add(parseSecuritySchemas(securityScheme, store));
            }
            componentsDescriptor.getSecuritySchemas().addAll(securitySchemaDescriptors);
        }

        //componentElementReader.readLinks(components, store, componentsDescriptor);
        if (components.getLinks() != null && !components.getLinks().isEmpty()) {
            List<LinkDescriptor> linkDescriptors = new ArrayList<>();
            for (Link link : components.getLinks().values()) {
                linkDescriptors.add(parseLinks(link, store));
            }
            componentsDescriptor.getLinks().addAll(linkDescriptors);
        }

        //componentElementReader.readPathItems(components, store, componentsDescriptor);
        if (components.getPathItems() != null && !components.getPathItems().isEmpty()) {
            Paths paths = new Paths();
            paths.putAll(components.getPathItems());
            componentsDescriptor.getPaths().addAll(parsePaths(paths, store));
        }

        //componentElementReader.readCallbacks(components, store, componentsDescriptor);
        if (components.getCallbacks() != null && !components.getCallbacks().isEmpty()) {
            List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
            for (Callback callback : components.getCallbacks().values()) {
                callbackDescriptors.add(parseCallbacks(callback, store));
            }
            componentsDescriptor.getCallBacks().addAll(callbackDescriptors);
        }

        //componentElementReader.readExamples(components, store, componentsDescriptor);
        if (components.getExamples() != null && !components.getExamples().isEmpty()) {
            List<ExampleDescriptor> exampleDescriptors = new ArrayList<>();
            for (Example example : components.getExamples().values()) {
                exampleDescriptors.add(parseExamples(example, store));
            }
            componentsDescriptor.getExamples().addAll(exampleDescriptors);
        }

        //componentElementReader.readResponses(components, store, componentsDescriptor);
        if (components.getResponses() != null && !components.getResponses().isEmpty()) {
            ApiResponses apiResponses = new ApiResponses();
            apiResponses.putAll(components.getResponses());
            List<ResponseDescriptor> responseDescriptors = parseResponses(apiResponses, store);
            componentsDescriptor.getResponses().addAll(responseDescriptors);
        }

        //componentElementReader.readParameters(components, store, componentsDescriptor);
        if (components.getParameters() != null && !components.getParameters().isEmpty()) {
            List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
            for (Parameter parameter : components.getParameters().values()) {
                List<Parameter> singleParameterList = new ArrayList<>();
                singleParameterList.add(parameter);
                parameterDescriptors.addAll(parseParameters(singleParameterList, store));
            }

            componentsDescriptor.getParameters().addAll(parameterDescriptors);
        }

        return componentsDescriptor;
    }
}
