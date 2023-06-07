package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.jqassistant.plugin.openapi.api.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Requires(FileDescriptor.class)
public class OpenAPIScannerPlugin extends AbstractScannerPlugin<FileResource, ContractDescriptor> {
    @Override
    public boolean accepts(FileResource fileResource, String path, Scope scope) throws IOException {
        return path.toLowerCase().endsWith(".yaml");  // TODO maybe add more testing
    }

    @Override
    public ContractDescriptor scan(FileResource fileResource, String path, Scope scope, Scanner scanner) throws IOException {
        System.out.print("scanning");
        ScannerContext context = scanner.getContext();
        final Store store = context.getStore();

        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        OpenAPI openAPI = parser.read(path); // TODO: Exception handling

        // Retrieve the scanned file node from the scanner context.
        final FileDescriptor fileDescriptor = context.getCurrentDescriptor();
        final ContractDescriptor contractDescriptor = store.addDescriptorType(fileDescriptor, ContractDescriptor.class);

        contractDescriptor.setApiVersion(openAPI.getOpenapi());

        // Read Info object
        Info info = openAPI.getInfo();
        if(info != null) {
            if(info.getTitle() != null)
                contractDescriptor.setTitle(info.getTitle());
            if (info.getDescription() != null)
                contractDescriptor.setDescription(info.getDescription());
            if (info.getVersion() != null)
                contractDescriptor.setApiVersion(info.getVersion());
            if (info.getContact() != null)
                contractDescriptor.setContact(parseContact(info.getContact(), store));
        }

        // Read all Tags
        if(openAPI.getTags() != null && !openAPI.getTags().isEmpty())
            contractDescriptor.getTags().addAll(parseTags(openAPI.getTags(), store));

        // Read all Servers
        if(openAPI.getServers() != null && !openAPI.getServers().isEmpty())
            contractDescriptor.getServers().addAll(parseSevers(openAPI.getServers(), store));

        //Read all Paths
        if(openAPI.getPaths() != null && !openAPI.getPaths().isEmpty())
            contractDescriptor.getPaths().addAll(parsePaths(openAPI.getPaths(), store));

        System.out.println("...finished");
        return contractDescriptor;
    }

    /**
     * Parses list of OpenApi Path objects
     *
     * @param paths list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    List<PathDescriptor> parsePaths (Paths paths, Store store){
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
    PathDescriptor parsePath (String pathUrl, PathItem pathItem, Store store){
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
    OperationDescriptor parseOperation(Operation operation, OperationDescriptor.HTTPMethod httpMethod, Store store){
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
     * Parses OpenApi requestBody object to internal object
     *
     * @param requestBody object to parse
     * @param store store object to create internal object from
     * @return parsed internal objects
     */
    RequestBodyDescriptor parseRequestBody(RequestBody requestBody, Store store){
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
     * Parses list of OpenApi Response objects
     *
     * @param responses list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    List<ResponseDescriptor> parseResponses(ApiResponses responses, Store store){
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
    ResponseDescriptor parseResponse(String statusCodeOrDefault, ApiResponse response, Store store){
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
     * Parses OpenApi Content object
     *
     * @param content object to parse
     * @param store store object to create internal object from
     * @return list of parsed internal mediaType objects
     */
    List<MediaTypeObjectDescriptor> parseContent(Content content, Store store){
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
    MediaTypeObjectDescriptor parseMediaTypeObject(String mediaType, MediaType mediaTypeObject, Store store){
        // TODO implement examples
        // TODO implement encoding
        // TODO implement schemas

        MediaTypeObjectDescriptor mediaTypeObjectDescriptor = store.create(MediaTypeObjectDescriptor.class);

        // read properties
        mediaTypeObjectDescriptor.setMediaType(mediaType);

        return mediaTypeObjectDescriptor;
    }

    /**
     * Parses list of OpenApi Parameter objects
     *
     * @param parameters list of objects to parse
     * @param store store object to create internal object from
     * @return list of parsed internal objects
     */
    List<ParameterDescriptor> parseParameters(List<Parameter> parameters, Store store){
        // TODO implement scanning of further props (might also be still missing in param descriptor)

        List<ParameterDescriptor> retParameters = new ArrayList<>();

        for (Parameter parameter : parameters){
            ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);

            // read properties
            if(parameter.getName() != null && !parameter.getName().isEmpty())
                parameterDescriptor.setName(parameter.getName());
            if(parameter.getIn() != null && !parameter.getIn().isEmpty())
                parameterDescriptor.setLocation(ParameterDescriptor.ParameterLocation
                        .valueOf(parameter.getIn()));
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
    ContactDescriptor parseContact(Contact contact, Store store){
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
    List<TagDescriptor> parseTags(List<Tag> tags, Store store){
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
    List<ServerDescriptor> parseSevers(List<Server> servers, Store store){
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
}
