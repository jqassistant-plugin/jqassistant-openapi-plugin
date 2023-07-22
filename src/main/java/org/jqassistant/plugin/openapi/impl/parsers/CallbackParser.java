package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.callbacks.Callback;
import org.jqassistant.plugin.openapi.api.model.CallbackDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CallbackParser {

    private CallbackParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<CallbackDescriptor> parseAll(Map<String, Callback> callbacksMap, Store store){
        List<CallbackDescriptor> callbackDescriptors = new ArrayList<>();
        callbacksMap.forEach((s, callback) -> callbackDescriptors.add(parseOne(callback, store)));
        return callbackDescriptors;
    }

    public static CallbackDescriptor parseOne(Callback callback, Store store){
        CallbackDescriptor callbackDescriptor = store.create(CallbackDescriptor.class);

        if(callback.get$ref() != null)
            callbackDescriptor.setRef(callback.get$ref());

        return callbackDescriptor;
    }
}
