package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.SchemaDescriptor;
import org.jqassistant.plugin.openapi.impl.util.InvalidSchemaRuntimeException;
import org.jqassistant.plugin.openapi.impl.util.Resolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;

class ResolverTest extends AbstractPluginIT {

    Resolver resolver;
    @BeforeEach
    void init(){
        store.beginTransaction();
        resolver = new Resolver(store);
    }

    @AfterEach
    void cleanup(){
        store.commitTransaction();
    }

    @Test
    void resolveNonExistingSchema(){
        String objectName = "foo";
        String exampeRef = "#/components/schemas/" + objectName;

        SchemaDescriptor schemaDescriptor = resolver.createIfAbsent(exampeRef);

        assertThat(schemaDescriptor).isNotNull();
        assertThat(schemaDescriptor.getName()).isEqualTo(objectName);
    }

    @Test
    void resolveExistingSchema(){
        String objectName = "foo";
        String objectNameEdit = objectName + "not";
        String objectRef = "#/components/schemas/" + objectName;

        resolver.createIfAbsent(objectRef).setName(objectNameEdit);

        SchemaDescriptor schemaDescriptor = resolver.createIfAbsent(objectRef);

        assertThat(schemaDescriptor).isNotNull();
        assertThat(schemaDescriptor.getName()).isEqualTo(objectNameEdit);

    }

    @Test
    void resolveInvalidSchemaReference(){

        String validSchemaReference = "#/components/schemas/foo";

        assertThrows(InvalidSchemaRuntimeException.class, () -> resolver.createIfAbsent("invalid/reference/Schema"));

        assertThrows(InvalidSchemaRuntimeException.class, () -> resolver.createIfAbsent(validSchemaReference + "/nope"));

        assertThrows(InvalidSchemaRuntimeException.class, () -> resolver.createIfAbsent("name"));

        assertThrows(InvalidSchemaRuntimeException.class, () -> resolver.createIfAbsent(null));


    }
}
