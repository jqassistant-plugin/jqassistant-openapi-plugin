package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.SchemaDescriptor;
import org.jqassistant.plugin.openapi.impl.util.Resolver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ResolverTest extends AbstractPluginIT {

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

        try {
            resolver.createIfAbsent("invalid/reference/Schema");
            Assertions.fail("resolved invalid reference");
        } catch (Exception e){
            assertThat(e).isNotNull();
        }

        try {
            resolver.createIfAbsent(validSchemaReference + "/nope");
            Assertions.fail("resolved invalid reference");
        } catch (Exception e){
            assertThat(e).isNotNull();
        }

        try {
            resolver.createIfAbsent("name");
            Assertions.fail("resolved invalid reference");
        } catch (Exception e){
            assertThat(e).isNotNull();
        }

    }
}
