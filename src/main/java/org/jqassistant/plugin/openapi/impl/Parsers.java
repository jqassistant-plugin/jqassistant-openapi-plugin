package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
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

    private Parsers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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
        List<ParameterDescriptor> retParameters = new ArrayList<>();
        for(Parameter parameter: parameters)
            retParameters.add(parseParameter(parameter, store));
        return retParameters;
    }

    static ParameterDescriptor parseParameter(Parameter parameter, Store store){
        // TODO implement scanning of further props (might also be still missing in param descriptor)
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

        return parameterDescriptor;
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
    static LinkDescriptor parseLink(Link link, Store store){
        LinkDescriptor linkDescriptor = store.create(LinkDescriptor.class);

        if(linkDescriptor.getOperationRef() != null){
            linkDescriptor.setOperationRef(link.getOperationRef());
        }

        return linkDescriptor;
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
     *
     Parses an OpenAPI Header object and creates a HeaderDescriptor based on the provided Header and Store.
     @param header The openAPI Header object to parse.
     @param store The Store object used to create the HeaderDescriptor.
     @return The parsed HeaderDescriptor object.
     */
    static HeaderDescriptor parseHeader(Header header, Store store){
        HeaderDescriptor headerDescriptor = store.create(HeaderDescriptor.class);
        if(header.getDescription() != null)
            headerDescriptor.setDescription((header.getDescription()));
        return headerDescriptor;
    }
}
