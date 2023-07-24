package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.headers.Header;
import org.jqassistant.plugin.openapi.api.model.HeaderDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderParser {

    private HeaderParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<HeaderDescriptor> parseAll(Map<String, Header> headersMap, Store store){
        return headersMap.values().stream().map(header -> parseOne(header, store)).collect(Collectors.toList());
    }

    public static HeaderDescriptor parseOne(Header header, Store store){
        HeaderDescriptor headerDescriptor = store.create(HeaderDescriptor.class);
        if(header.getDescription() != null)
            headerDescriptor.setDescription((header.getDescription()));
        return headerDescriptor;
    }
}
