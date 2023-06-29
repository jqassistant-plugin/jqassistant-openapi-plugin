package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.checkerframework.checker.units.qual.C;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;

public class OpenAPIElementReader {

    public static void readInfo(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        Info info = openAPI.getInfo();
        if(info != null) {
            if(info.getTitle() != null)
                contractDescriptor.setTitle(info.getTitle());
            if (info.getDescription() != null)
                contractDescriptor.setDescription(info.getDescription());
            if (info.getVersion() != null)
                contractDescriptor.setApiVersion(info.getVersion());
            if (info.getContact() != null)
                contractDescriptor.setContact(OpenAPIScannerPlugin.parseContact(info.getContact(), store));
        }
    }

    public static void readTags(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getTags() != null && !openAPI.getTags().isEmpty())
            contractDescriptor.getTags().addAll(OpenAPIScannerPlugin.parseTags(openAPI.getTags(), store));
    }

    public static void readServers(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getServers() != null && !openAPI.getServers().isEmpty())
            contractDescriptor.getServers().addAll(OpenAPIScannerPlugin.parseSevers(openAPI.getServers(), store));
    }

    public static void readPaths(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if(openAPI.getPaths() != null && !openAPI.getPaths().isEmpty())
            contractDescriptor.getPaths().addAll(OpenAPIScannerPlugin.parsePaths(openAPI.getPaths(), store));
    }

    public static void readComponents(OpenAPI openAPI, ContractDescriptor contractDescriptor, Store store){
        if (openAPI.getComponents() != null)
            contractDescriptor.setComponents(ComponentElementReader.parseComponents(openAPI.getComponents(), store));
    }
}
