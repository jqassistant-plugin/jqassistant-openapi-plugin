package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.headers.Header;
import org.jqassistant.plugin.openapi.api.model.HeaderDescriptor;
import org.jqassistant.plugin.openapi.impl.jsonschema.JsonSchemaParser;
import org.jqassistant.plugin.openapi.impl.util.Resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeaderParser {

    private HeaderParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<HeaderDescriptor> parseAll(Map<String, Header> headersMap, Store store){
        return headersMap.entrySet().stream().map(headerEntry -> parseOne(headerEntry.getKey(), headerEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static HeaderDescriptor parseOne(String name, Header header, Store store){
        HeaderDescriptor headerDescriptor = store.create(HeaderDescriptor.class);

        JsonSchemaParser schemaParser = new JsonSchemaParser(new Resolver(store), store);

        headerDescriptor.setName(name);

        if(header.getDescription() != null)
            headerDescriptor.setDescription((header.getDescription()));

        if(header.getRequired() != null)
            headerDescriptor.setIsRequired((header.getRequired()));
        if(header.getDeprecated() != null)
            headerDescriptor.setIsDeprecated(header.getDeprecated());
        if(header.getStyle() != null)
            headerDescriptor.setStyle(header.getStyle());
        if(header.getExplode() != null)
            headerDescriptor.setExplode(header.getExplode());
        if(header.getExample() != null)
            headerDescriptor.setExample(header.getExample());
        if(header.getSchema() != null)
            headerDescriptor.setSchema(schemaParser.parseSchema(header.getSchema(), header.getSchema().getName()));
        if(header.getExamples() != null)
            headerDescriptor.getExamples().addAll(ExampleParser.parseAll(header.getExamples(), store));
        if(header.getContent() != null)
            headerDescriptor.getContent().addAll(MediaTypeParser.parseAll(header.getContent(), store));

        return headerDescriptor;
    }
}
