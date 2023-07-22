package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.jqassistant.plugin.openapi.api.model.OperationDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathDescriptor;

import java.util.*;

public class PathParser {

    private PathParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<PathDescriptor> parseAll(Map<String, PathItem> pathsMap, Store store){
        List<PathDescriptor> pathDescriptors = new ArrayList<>();
        pathsMap.forEach((pathUrl, pathItem) -> pathDescriptors.add(parseOne(pathUrl, pathItem, store)));
        return pathDescriptors;
    }

    public static PathDescriptor parseOne(String pathUrl, PathItem pathItem, Store store) {
        PathDescriptor pathDescriptor = store.create(PathDescriptor.class);

        //set path properties
        setProperties(pathDescriptor, pathItem, pathUrl);

        // parse operations
        EnumMap<OperationDescriptor.HTTPMethod, Operation> operations = combineOperations(pathItem);
        pathDescriptor.getOperations().addAll(parseOperations(operations, store));

        // parse servers
        if (pathItem.getServers() != null)
            pathDescriptor.getServers().addAll(ServerParser.parseAll(pathItem.getServers(), store));

        // parse parameters
        if (pathItem.getParameters() != null)
            pathDescriptor.getParameters().addAll(ParameterParser.parseAll(pathItem.getParameters(), store));

        return pathDescriptor;
    }

    private static void setProperties(PathDescriptor pathDescriptor, PathItem pathItem, String pathUrl){
        pathDescriptor.setPathUrl(pathUrl);
        if(pathItem.get$ref() != null && !pathItem.get$ref().isEmpty())
            pathDescriptor.setReferenceString(pathItem.get$ref());
        if(pathItem.getSummary() != null && !pathItem.getSummary().isEmpty())
            pathDescriptor.setSummary(pathItem.getSummary());
        if(pathItem.getDescription() != null && !pathItem.getDescription().isEmpty())
            pathDescriptor.setDescription(pathItem.getDescription());
    }

    private static EnumMap<OperationDescriptor.HTTPMethod, Operation> combineOperations(PathItem pathItem){
        EnumMap<OperationDescriptor.HTTPMethod, Operation> operations = new EnumMap<>(OperationDescriptor.HTTPMethod.class);

        if (pathItem.getGet() != null)
            operations.put(OperationDescriptor.HTTPMethod.GET, pathItem.getGet());
        if (pathItem.getPut() != null)
            operations.put(OperationDescriptor.HTTPMethod.PUT, pathItem.getPut());
        if (pathItem.getPost() != null)
            operations.put(OperationDescriptor.HTTPMethod.POST, pathItem.getPost());
        if (pathItem.getDelete() != null)
            operations.put(OperationDescriptor.HTTPMethod.DELETE, pathItem.getDelete());
        if (pathItem.getOptions() != null)
            operations.put(OperationDescriptor.HTTPMethod.OPTIONS, pathItem.getOptions());
        if (pathItem.getHead() != null)
            operations.put(OperationDescriptor.HTTPMethod.HEAD, pathItem.getHead());
        if (pathItem.getPatch() != null)
            operations.put(OperationDescriptor.HTTPMethod.PATCH, pathItem.getPatch());
        if (pathItem.getTrace() != null)
            operations.put(OperationDescriptor.HTTPMethod.TRACE, pathItem.getTrace());

        return operations;
    }

    private static List<OperationDescriptor> parseOperations(Map<OperationDescriptor.HTTPMethod, Operation> operationsMap, Store store){
        List<OperationDescriptor> operationDescriptors = new ArrayList<>();
        operationsMap.forEach((httpMethod, operation) -> operationDescriptors.add(parseOperation(httpMethod, operation, store)));
        return operationDescriptors;
    }

    private static OperationDescriptor parseOperation(OperationDescriptor.HTTPMethod httpMethod, Operation operation, Store store){
        OperationDescriptor operationDescriptor = store.create(OperationDescriptor.class);

        // read properties
        operationDescriptor.setType(httpMethod);
        if(operation.getSummary() != null && !operation.getSummary().isEmpty())
            operationDescriptor.setSummary(operation.getSummary());
        if(operation.getDescription() != null && !operation.getDescription().isEmpty())
            operationDescriptor.setDescription(operation.getDescription());
        if(operation.getOperationId() != null && !operation.getOperationId().isEmpty())
            operationDescriptor.setOperationId(operation.getOperationId());
        if(operation.getDeprecated() != null)
            operationDescriptor.setIsDeprecated(operation.getDeprecated());
        else
            operationDescriptor.setIsDeprecated(false); // Default Value

        // read responses
        if(operation.getResponses() != null && !operation.getResponses().isEmpty())
            operationDescriptor.getResponses().addAll(ResponseParser.parseAll(operation.getResponses(), store));

        // read requestBody
        if(operation.getRequestBody() != null)
            operationDescriptor.setRequestBody(RequestBodyParser.parseOne(operation.getRequestBody(), store));

        return operationDescriptor;
    }
}
