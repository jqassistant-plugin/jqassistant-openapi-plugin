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

        if(requestBody.getDescription() != null)
            requestBodyDescriptor.setDescription(requestBody.getDescription());
        requestBodyDescriptor.getMediaTypeObjects().addAll(MediaTypeParser.parseAll(requestBody.getContent(), store)); // required
        if(requestBody.getRequired() != null)
            requestBodyDescriptor.setIsRequired(requestBody.getRequired());

        return requestBodyDescriptor;
    }
}
