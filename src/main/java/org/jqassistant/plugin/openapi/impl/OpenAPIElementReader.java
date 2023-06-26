package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;

public class OpenAPIElementReader {

    private final OpenAPIScannerPlugin openAPIScannerPlugin;

    public OpenAPIElementReader(OpenAPIScannerPlugin openAPIScannerPlugin) {
        this.openAPIScannerPlugin = openAPIScannerPlugin;
    }


    public void readInfo(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        Info info = openAPI.getInfo();
        if(info != null) {
            if(info.getTitle() != null)
                contractDescriptor.setTitle(info.getTitle());
            if (info.getDescription() != null)
                contractDescriptor.setDescription(info.getDescription());
            if (info.getVersion() != null)
                contractDescriptor.setApiVersion(info.getVersion());
            if (info.getContact() != null)
                contractDescriptor.setContact(openAPIScannerPlugin.parseContact(info.getContact(), store));
        }
    }

    public void readTags(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getTags() != null && !openAPI.getTags().isEmpty())
            contractDescriptor.getTags().addAll(openAPIScannerPlugin.parseTags(openAPI.getTags(), store));
    }

    public void readServers(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getServers() != null && !openAPI.getServers().isEmpty())
            contractDescriptor.getServers().addAll(openAPIScannerPlugin.parseSevers(openAPI.getServers(), store));
    }

    public void readPaths(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getPaths() != null && !openAPI.getPaths().isEmpty())
            contractDescriptor.getPaths().addAll(openAPIScannerPlugin.parsePaths(openAPI.getPaths(), store));
    }

    public void readComponents(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if (openAPI.getComponents() != null)
            contractDescriptor.setComponents(openAPIScannerPlugin.parseComponents(openAPI.getComponents(), store));
    }
}
