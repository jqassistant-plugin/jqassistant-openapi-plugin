package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Schema;
import org.jqassistant.plugin.openapi.api.model.*;

public class Parsers {

    private Parsers() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    /**
     *
     Parses an OpenAPI Schema object and creates a SchemaDescriptor based on the provided Schema and Store.
     @param schema The Schema object to parse.
     @param store The Store object used to create the SchemaDescriptor.
     @return The parsed SchemaDescriptor object.
     */
    static SchemaDescriptor parseSchema(Schema<?> schema, Store store){
        SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);

        if(schemaDescriptor.getName() != null){
            schemaDescriptor.setName(schema.getName());
        }

        return schemaDescriptor;
    }
}
