package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.parameters.RequestBody;
import org.jqassistant.plugin.openapi.api.model.RequestBodyDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestBodyParser {

    private RequestBodyParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<RequestBodyDescriptor> parseAll(Map<String, RequestBody> requestBodiesMap, Store store){
        return requestBodiesMap.values().stream().map(requestBody -> parseOne(requestBody, store)).collect(Collectors.toList());
    }

    public static RequestBodyDescriptor parseOne(RequestBody requestBody, Store store){
        RequestBodyDescriptor requestBodyDescriptor = store.create(RequestBodyDescriptor.class);

        // read properties
        if(requestBody.getDescription() != null && !requestBody.getDescription().isEmpty())
            requestBodyDescriptor.setDescription(requestBody.getDescription());
        if(requestBody.getRequired() != null)
            requestBodyDescriptor.setIsRequired(requestBody.getRequired());

        // read content
        if(requestBody.getContent() != null && !requestBody.getContent().isEmpty())
            requestBodyDescriptor.getMediaTypeObjects().addAll(MediaTypeParser.parseAll(requestBody.getContent(), store));

        return requestBodyDescriptor;
    }
}
