package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Schema;
import org.jqassistant.plugin.openapi.api.model.SchemaDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SchemaParser {

    private SchemaParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<SchemaDescriptor> parseAll(Map<String, Schema> schemasMap, Store store){
        return schemasMap.values().stream().map(schema -> parseOne(schema, store)).collect(Collectors.toList());
    }

    public static SchemaDescriptor parseOne(Schema<?> schema, Store store){
        SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);

        if(schemaDescriptor.getName() != null)
            schemaDescriptor.setName(schema.getName());

        return schemaDescriptor;
    }
}
