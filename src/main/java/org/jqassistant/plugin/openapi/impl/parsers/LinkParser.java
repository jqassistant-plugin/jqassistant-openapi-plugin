package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.links.Link;
import org.jqassistant.plugin.openapi.api.model.LinkDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LinkParser {

    private LinkParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<LinkDescriptor> parseAll(Map<String, Link> linksMap, Store store){
        List<LinkDescriptor> linkDescriptors = new ArrayList<>();
        linksMap.forEach((s, link) -> linkDescriptors.add(parseOne(link, store)));
        return linkDescriptors;
    }

    public static LinkDescriptor parseOne(Link link, Store store){
        LinkDescriptor linkDescriptor = store.create(LinkDescriptor.class);

        if(linkDescriptor.getOperationRef() != null)
            linkDescriptor.setOperationRef(link.getOperationRef());

        return linkDescriptor;
    }
}
