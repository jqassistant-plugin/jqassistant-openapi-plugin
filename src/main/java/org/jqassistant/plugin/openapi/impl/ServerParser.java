package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.servers.Server;
import org.jqassistant.plugin.openapi.api.model.ServerDescriptor;

import java.util.List;
import java.util.stream.Collectors;

public class ServerParser {

    private ServerParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<ServerDescriptor> parseAll(List<Server> servers, Store store){
        // TODO evaluate with team
        return servers.stream().map(server -> parseOne(server, store)).collect(Collectors.toList());
    }

    public static ServerDescriptor parseOne(Server server, Store store){
        ServerDescriptor serverDescriptor = store.create(ServerDescriptor.class);

        serverDescriptor.setUrl(server.getUrl());

        if(server.getDescription() != null)
            serverDescriptor.setDescription(server.getDescription());

        return serverDescriptor;
    }
}
