package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.jqassistant.plugin.openapi.api.model.OperationDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathItemDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathsDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class PathsParser {

    private static final Logger LOG = LoggerFactory.getLogger(PathsParser.class);

    private PathsParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static PathsDescriptor parse(Map<String, PathItem> pathsMap, Store store){
        PathsDescriptor pathsDescriptor = store.create(PathsDescriptor.class);
        pathsDescriptor.getPathItems().addAll(parsePathItems(pathsMap, store));
        return pathsDescriptor;
    }

    public static List<PathItemDescriptor> parsePathItems(Map<String, PathItem> pathsMap, Store store){
        return pathsMap.entrySet().stream().map(pathItemEntry -> parsePathItem(pathItemEntry.getKey(), pathItemEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static PathItemDescriptor parsePathItem(String pathUrl, PathItem pathItem, Store store) {
        PathItemDescriptor pathItemDescriptor = store.create(PathItemDescriptor.class);

        pathItemDescriptor.setPathUrl(pathUrl);

        if(pathItem == null){
            LOG.warn("pathItem <{}> does not contain any data -> ignoring it", pathUrl);
            return pathItemDescriptor;
        }

        //set path properties
        setProperties(pathItemDescriptor, pathItem);

        // parse operations
        EnumMap<OperationDescriptor.HTTPMethod, Operation> operations = combineOperations(pathItem);
        pathItemDescriptor.getOperations().addAll(parseOperations(operations, store));

        // parse servers
        if (pathItem.getServers() != null)
            pathItemDescriptor.getServers().addAll(ServerParser.parseAll(pathItem.getServers(), store));

        // parse parameters
        if (pathItem.getParameters() != null)
            pathItemDescriptor.getParameters().addAll(ParameterParser.parseAll(pathItem.getParameters(), store));

        return pathItemDescriptor;
    }

    private static void setProperties(PathItemDescriptor pathItemDescriptor, PathItem pathItem){
        if(pathItem.get$ref() != null && !pathItem.get$ref().isEmpty())
            pathItemDescriptor.setReferenceString(pathItem.get$ref());
        if(pathItem.getSummary() != null && !pathItem.getSummary().isEmpty())
            pathItemDescriptor.setSummary(pathItem.getSummary());
        if(pathItem.getDescription() != null && !pathItem.getDescription().isEmpty())
            pathItemDescriptor.setDescription(pathItem.getDescription());
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
        return operationsMap.entrySet().stream().map(operationEntry -> parseOperation(operationEntry.getKey(), operationEntry.getValue(), store)).collect(Collectors.toList());
    }

    private static OperationDescriptor parseOperation(OperationDescriptor.HTTPMethod httpMethod, Operation operation, Store store){
        OperationDescriptor operationDescriptor = store.create(OperationDescriptor.class);

        // read properties
        operationDescriptor.setType(httpMethod);
        if(operation.getSummary() != null && !operation.getSummary().isEmpty())
            operationDescriptor.setSummary(operation.getSummary());
        if(operation.getDescription() != null && !operation.getDescription().isEmpty())
            operationDescriptor.setDescription(operation.getDescription());
        if(operation.getExternalDocs() != null)
            operationDescriptor.setExternalDocs(ExternalDocsParser.parseOne(operation.getExternalDocs(), store));
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

        // read parameters
        if(operation.getParameters() != null)
            operationDescriptor.getParameters().addAll(ParameterParser.parseAll(operation.getParameters(), store));

        // read tags
        if(operation.getTags() != null)
            operationDescriptor.getTags().addAll(TagParser.parseAll(operation.getTags(), store));

        return operationDescriptor;
    }
}
