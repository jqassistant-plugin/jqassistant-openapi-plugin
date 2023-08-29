package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.links.Link;
import org.jqassistant.plugin.openapi.api.model.LinkDescriptor;
import org.jqassistant.plugin.openapi.api.model.LinkParameterDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkParser {

    private static final String NULL_STR = "null";

    private LinkParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<LinkDescriptor> parseAll(Map<String, Link> linksMap, Store store){
        return linksMap.entrySet().stream().map(linkEntry -> parseOne(linkEntry.getKey(), linkEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static LinkDescriptor parseOne(String linkName, Link link, Store store){
        LinkDescriptor linkDescriptor = store.create(LinkDescriptor.class);

        linkDescriptor.setName(linkName);

        if(link.getOperationRef() != null)
            linkDescriptor.setOperationRef(link.getOperationRef());
        if(link.getOperationId() != null)
            linkDescriptor.setOperationId(link.getOperationId());
        if(link.getParameters() != null)
            linkDescriptor.getParameters().addAll(parseLinkParameters(link.getParameters(), store));
        if(link.getRequestBody() != null)
            linkDescriptor.setRequestBody(link.getRequestBody());
        if(link.getDescription() != null)
            linkDescriptor.setDescription(link.getDescription());
        if(link.getServer() != null)
            linkDescriptor.setServer(ServerParser.parseOne(link.getServer(), store));

        return linkDescriptor;
    }

    private static List<LinkParameterDescriptor> parseLinkParameters(Map<String, String> parameters, Store store){
        List<LinkParameterDescriptor> parameterDescriptors = new ArrayList<>();
        for(Map.Entry<String, String> parameter: parameters.entrySet()){
            LinkParameterDescriptor parameterDescriptor = store.create(LinkParameterDescriptor.class);
            parameterDescriptor.setName(parameter.getKey());
            if(parameter.getValue() != null && !parameter.getValue().equals(NULL_STR))
                parameterDescriptor.setValue(parameter.getValue());
            parameterDescriptors.add(parameterDescriptor);
        }
        return parameterDescriptors;
    }
}
