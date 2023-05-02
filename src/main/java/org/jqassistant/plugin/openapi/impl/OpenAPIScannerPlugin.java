package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.jqassistant.plugin.openapi.api.model.ContactDescriptor;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.ServerDescriptor;
import org.jqassistant.plugin.openapi.api.model.TagDescriptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenAPIScannerPlugin extends AbstractScannerPlugin<FileResource, ContractDescriptor> {
    @Override
    public boolean accepts(FileResource fileResource, String path, Scope scope) throws IOException {
        return path.toLowerCase().endsWith(".yaml");  // TODO maybe add more testing
    }

    @Override
    public ContractDescriptor scan(FileResource fileResource, String path, Scope scope, Scanner scanner) throws IOException {

        ScannerContext context = scanner.getContext();
        final Store store = context.getStore();

        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        OpenAPI openAPI = parser.read(path); // TODO: Exception handling

        ContractDescriptor contractDescriptor = store.create(ContractDescriptor.class);

        // Read Info object
        Info info = openAPI.getInfo();
        contractDescriptor.setTitle(info.getTitle());
        contractDescriptor.setDescription(info.getDescription());
        //contractDescriptor.setOpenApiVersion(); // TODO
        contractDescriptor.setApiVersion(info.getVersion());
        contractDescriptor.setFileName(fileResource.getFile().getName());
        contractDescriptor.setContact(parseContact(info.getContact(), store));

        // Read all Tags
        contractDescriptor.getTags().addAll(parseTags(openAPI.getTags(), store));

        // Read all Servers
        contractDescriptor.getServers().addAll(parseSevers(openAPI.getServers(), store));

        return contractDescriptor;
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

        contactDescriptor.setName(contact.getName());
        contactDescriptor.setEmail(contact.getEmail());
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
            tagDescriptor.setTag(tag.getName());
            tagDescriptor.setDescription(tag.getDescription());
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
            serverDescriptor.setDescription(server.getDescription());
        }

        return retServers;
    }
}
