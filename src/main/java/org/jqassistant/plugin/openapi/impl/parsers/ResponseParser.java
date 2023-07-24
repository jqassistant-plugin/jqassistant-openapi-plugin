package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.jqassistant.plugin.openapi.api.model.ResponseDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseParser {

    private static final String DEFAULT_STR = "default";

    private ResponseParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<ResponseDescriptor> parseAll(Map<String, ApiResponse> responsesMap, Store store){
        return responsesMap.entrySet().stream().map(responseEntry -> parseOne(responseEntry.getKey(), responseEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static ResponseDescriptor parseOne(String statusCodeOrDefault, ApiResponse response, Store store){
        ResponseDescriptor responseDescriptor = store.create(ResponseDescriptor.class);

        // read statusCode and default flag
        if(statusCodeOrDefault.equals(DEFAULT_STR))
            responseDescriptor.setIsDefault(true);
        else {
            responseDescriptor.setIsDefault(false);
            responseDescriptor.setStatusCode(statusCodeOrDefault);
        }

        // read properties
        if(response.getDescription() != null && !response.getDescription().isEmpty())
            responseDescriptor.setDescription(response.getDescription());

        // read content
        if(response.getContent() != null)
            responseDescriptor.getMediaTypeObject().addAll(MediaTypeParser.parseAll(response.getContent(), store));

        return responseDescriptor;

    }
}