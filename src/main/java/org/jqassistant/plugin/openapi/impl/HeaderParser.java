package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.headers.Header;
import org.jqassistant.plugin.openapi.api.model.HeaderDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HeaderParser {

    private HeaderParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<HeaderDescriptor> parseAll(Map<String, Header> headersMap, Store store){
        List<HeaderDescriptor> headerDescriptors = new ArrayList<>();
        headersMap.forEach((s, header) -> headerDescriptors.add(parseOne(header, store)));
        return headerDescriptors;
    }

    public static HeaderDescriptor parseOne(Header header, Store store){
        HeaderDescriptor headerDescriptor = store.create(HeaderDescriptor.class);
        if(header.getDescription() != null)
            headerDescriptor.setDescription((header.getDescription()));
        return headerDescriptor;
    }
}
