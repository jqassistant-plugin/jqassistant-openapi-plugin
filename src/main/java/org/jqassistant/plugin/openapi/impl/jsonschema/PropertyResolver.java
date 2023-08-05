package org.jqassistant.plugin.openapi.impl.jsonschema;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.plugin.openapi.api.model.jsonschema.PropertyDescriptor;

import java.util.HashMap;
import java.util.Map;

class PropertyResolver {

    private final Map<String, PropertyDescriptor> propertyMap;
    private final Store store;

    public PropertyResolver(Store store) {
        this.propertyMap = new HashMap<>();
        this.store = store;
    }

    PropertyDescriptor resolve(String name){
        return resolve(name, PropertyDescriptor.class);
    }

    PropertyDescriptor resolve(String name, boolean doNotCache){
        return resolve(name, PropertyDescriptor.class, doNotCache);
    }

    <T extends PropertyDescriptor> T resolve(String name, Class<T> clazz) {
        return resolve(name, clazz, false);
    }

    <T extends PropertyDescriptor> T resolve(String name, Class<T> clazz, boolean doNotCache){

        T returnvalue;

        if (doNotCache){
            returnvalue = store.create(clazz);
        } else {
           returnvalue  = (T) propertyMap.computeIfAbsent(name, (key) -> {
                T propertyDescriptor = store.create(clazz);
                return propertyDescriptor;
            });
        }

        if (name != null)
            returnvalue.setName(name);

        return returnvalue;
    }
}
