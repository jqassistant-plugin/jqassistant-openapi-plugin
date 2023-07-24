package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.MediaType;
import org.jqassistant.plugin.openapi.api.model.MediaTypeObjectDescriptor;

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

        // read properties
        mediaTypeObjectDescriptor.setMediaType(mediaTypeType);
        return mediaTypeObjectDescriptor;
    }
}