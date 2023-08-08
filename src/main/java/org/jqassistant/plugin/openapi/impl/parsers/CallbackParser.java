package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.jqassistant.plugin.openapi.api.model.CallbackDescriptor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CallbackParser {

    private CallbackParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<CallbackDescriptor> parseAll(Map<String, Callback> callbacksMap, Store store){
        return callbacksMap.entrySet().stream().map(callbackEntry -> parseOne(callbackEntry.getKey(), callbackEntry.getValue(), store)).collect(Collectors.toList());
    }

    public static CallbackDescriptor parseOne(String name, Callback callback, Store store){
        CallbackDescriptor callbackDescriptor = store.create(CallbackDescriptor.class);

        callbackDescriptor.setName(name);

        if(callback.get$ref() != null)
            callbackDescriptor.setRef(callback.get$ref());

        callbackDescriptor.getPathItems().addAll(PathsParser.parsePathItems(callback, store));

        return callbackDescriptor;
    }
}
