package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ComponentsCountTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @BeforeEach
    void init(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-components-counttest.yaml");
        contract = getScanner().scan(file, "/example-components-counttest.yaml", DefaultScope.NONE);

        store.beginTransaction();

        assertThat(contract).isNotNull();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void testRequestBodies() {
        List<RequestBodyDescriptor> requestBodies = contract.getComponents().getRequestBodies();
        assertThat(requestBodies).hasSize(0);
    }

    @Test
    void testHeaders() {
        List<HeaderDescriptor> headers = contract.getComponents().getHeaders();
        assertThat(headers).hasSize(0);
    }

    @Test
    void testExamples() {
        List<ExampleDescriptor> examples = contract.getComponents().getExamples();
        assertThat(examples).hasSize(2);
    }

    @Test
    void testSchemas() {
        List<SchemaDescriptor> schemas = contract.getComponents().getSchemas();
        assertThat(schemas).hasSize(0);
    }

}
