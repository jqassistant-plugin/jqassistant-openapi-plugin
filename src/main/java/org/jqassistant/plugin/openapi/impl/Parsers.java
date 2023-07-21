package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.jqassistant.plugin.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;

public class Parsers {

    private Parsers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
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
    static SecuritySchemaDescriptor parseSecurityScheme(SecurityScheme securityScheme, Store store){
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
