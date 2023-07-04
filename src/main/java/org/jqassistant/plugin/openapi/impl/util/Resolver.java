package org.jqassistant.plugin.openapi.impl.util;

import com.buschmais.jqassistant.core.store.api.Store;
import org.jqassistant.plugin.openapi.api.model.SchemaDescriptor;

import java.util.HashMap;
import java.util.Map;

public class Resolver {

    private final Map<String, SchemaDescriptor> schemaMap;
    private final Store store;

    public Resolver(Store store){
        this.store = store;
        this.schemaMap = new HashMap<>();
    }

    public SchemaDescriptor createIfAbsent(String ref){

        String name = refToName(ref);

        if (schemaMap.containsKey(name)){
            return schemaMap.get(name);
        } else {
            SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);
            schemaDescriptor.setName(name);

            schemaMap.put(name, schemaDescriptor);

            return schemaDescriptor;
        }
    }

    /**
     * Parses ref string to object name
     *
     * @param ref ref string in form of "#/components/schemas/example"
     * @return the object name
     */
    private static String refToName(String ref){
        String[] splits = ref.split("/");
        if (splits.length != 4
        || !splits[0].equals("#")
        || !splits[1].equals("components")
        || !splits[2].equals("schemas")){
            throw new InvalidSchemaRuntimeException("Invalid Schema Reference: " + ref);
        }

        return splits[3];
    }
}
