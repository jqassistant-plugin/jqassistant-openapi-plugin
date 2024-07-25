package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import org.jqassistant.plugin.openapi.api.model.ServerDescriptor;
import org.jqassistant.plugin.openapi.api.model.ServerVariableDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerParser {

    private ServerParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<ServerDescriptor> parseAll(List<Server> servers, Store store){
        return servers.stream().map(server -> parseOne(server, store)).collect(Collectors.toList());
    }

    public static ServerDescriptor parseOne(Server server, Store store){
        ServerDescriptor serverDescriptor = store.create(ServerDescriptor.class);

        serverDescriptor.setUrl(server.getUrl());

        if(server.getDescription() != null)
            serverDescriptor.setDescription(server.getDescription());
        if(server.getVariables() != null)
            serverDescriptor.getVariables().addAll(parseServerVariables(server.getVariables(), store));

        return serverDescriptor;
    }

    private static List<ServerVariableDescriptor> parseServerVariables(Map<String, ServerVariable> serverVariablesMap, Store store){
        return serverVariablesMap.entrySet().stream().map(serverVariableEntry -> parseServerVariable(serverVariableEntry.getKey(), serverVariableEntry.getValue(), store)).collect(Collectors.toList());
    }

    private static ServerVariableDescriptor parseServerVariable(String variableName, ServerVariable variable, Store store){
        ServerVariableDescriptor serverVariableDescriptor = store.create(ServerVariableDescriptor.class);

        serverVariableDescriptor.setName(variableName);

        if(variable.getEnum() != null)
            serverVariableDescriptor.setPossibleValues(variable.getEnum().toArray(new String[0]));
        if(variable.getDefault() != null)
            serverVariableDescriptor.setDefault(variable.getDefault());
        if(variable.getDescription() != null)
            serverVariableDescriptor.setDescription(variable.getDescription());

        return serverVariableDescriptor;
    }

}
