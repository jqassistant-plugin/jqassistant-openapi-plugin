package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.MediaType;
import org.jqassistant.plugin.openapi.api.model.EncodingDescriptor;
import org.jqassistant.plugin.openapi.api.model.MediaTypeObjectDescriptor;
import org.jqassistant.plugin.openapi.impl.jsonschema.JsonSchemaParser;
import org.jqassistant.plugin.openapi.impl.util.Resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MediaTypeParser {

    private MediaTypeParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<MediaTypeObjectDescriptor> parseAll(Map<String, MediaType> mediaTypesMap, Store store){
        return mediaTypesMap.entrySet().stream().map(mediaTypeEntry -> parseOne(mediaTypeEntry.getKey(), mediaTypeEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static MediaTypeObjectDescriptor parseOne(String mediaTypeType, MediaType mediaType, Store store){
        MediaTypeObjectDescriptor mediaTypeObjectDescriptor = store.create(MediaTypeObjectDescriptor.class);
        JsonSchemaParser schemaParser = new JsonSchemaParser(new Resolver(store), store);

        mediaTypeObjectDescriptor.setMediaType(mediaTypeType);

        if(mediaType.getSchema() != null)
            mediaTypeObjectDescriptor.setSchema(schemaParser.parseOneSchema(mediaType.getSchema(), null));
        if(mediaType.getExampleSetFlag())
            mediaTypeObjectDescriptor.setExample(mediaType.getExample());
        if(mediaType.getExamples() != null)
            mediaTypeObjectDescriptor.getExamples().addAll(ExampleParser.parseAll(mediaType.getExamples(), store));
        if(mediaType.getEncoding() != null)
            mediaTypeObjectDescriptor.getEncodings().addAll(parseEncodings(mediaType.getEncoding(), store));

        return mediaTypeObjectDescriptor;
    }

    private static List<EncodingDescriptor> parseEncodings(Map<String, Encoding> encodingsMap, Store store){
        return encodingsMap.entrySet().stream().map(encodingEntry -> parseEncoding(encodingEntry.getKey(), encodingEntry.getValue(), store)).collect(Collectors.toList());
    }

    private static EncodingDescriptor parseEncoding(String propertyName, Encoding encoding, Store store){
        EncodingDescriptor encodingDescriptor = store.create(EncodingDescriptor.class);

        encodingDescriptor.setPropertyName(propertyName);

        if(encoding.getContentType() != null)
            encodingDescriptor.setContentType(encoding.getContentType());
        if(encoding.getHeaders() != null)
            encodingDescriptor.getHeaders().addAll(HeaderParser.parseAll(encoding.getHeaders(), store));
        if(encoding.getStyle() != null)
            encodingDescriptor.setStyle(encoding.getStyle());
        if(encoding.getExplode() != null)
            encodingDescriptor.setExplode(encoding.getExplode());
        if(encoding.getAllowReserved() != null)
            encodingDescriptor.setAllowsReserved(encoding.getAllowReserved());

        return encodingDescriptor;
    }
}