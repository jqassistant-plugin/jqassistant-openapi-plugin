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
        return requestBodiesMap.entrySet().stream().map(requestBodyEntry -> parseOne(requestBodyEntry.getKey(), requestBodyEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static RequestBodyDescriptor parseOne(RequestBody requestBody, Store store){
        return parseOne(null, requestBody, store);
    }

    public static RequestBodyDescriptor parseOne(String name, RequestBody requestBody, Store store){
        RequestBodyDescriptor requestBodyDescriptor = store.create(RequestBodyDescriptor.class);

        requestBodyDescriptor.setName(name); // requestBodiesMap key has to be present in entry

        if(requestBody.getDescription() != null)
            requestBodyDescriptor.setDescription(requestBody.getDescription());
        if(requestBody.getContent() != null)
            requestBodyDescriptor.getMediaTypes().addAll(MediaTypeParser.parseAll(requestBody.getContent(), store));
        if(requestBody.getRequired() != null)
            requestBodyDescriptor.setIsRequired(requestBody.getRequired());

        return requestBodyDescriptor;
    }
}
