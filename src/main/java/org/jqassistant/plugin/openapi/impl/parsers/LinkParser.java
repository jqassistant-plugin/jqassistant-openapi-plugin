package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.links.Link;
import org.jqassistant.plugin.openapi.api.model.LinkDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LinkParser {

    private LinkParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<LinkDescriptor> parseAll(Map<String, Link> linksMap, Store store){
        return linksMap.values().stream().map(link -> parseOne(link, store)).collect(Collectors.toList());
    }

    public static LinkDescriptor parseOne(Link link, Store store){
        LinkDescriptor linkDescriptor = store.create(LinkDescriptor.class);

        if(linkDescriptor.getOperationRef() != null)
            linkDescriptor.setOperationRef(link.getOperationRef());

        return linkDescriptor;
    }
}
