package org.jqassistant.plugin.asyncapi.parser;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.asyncapi.api.AsyncApiScope;
import org.jqassistant.plugin.asyncapi.api.model.ContractDescriptor;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class ContractIT extends AbstractPluginIT {

    @Test
    @TestStore(type = TestStore.Type.REMOTE)
    void contractWithOnlyOpenApiVersion() {
        // Given
        File file = new File(getClassesDirectory(ContractIT.class), "exampleAsyncAPI.yaml");

        // When
        ContractDescriptor contract = getScanner().scan(file, "exampleAsyncAPI.yaml", AsyncApiScope.CONTRACT);

        // Then
        assertThat(contract).isNotNull();
    }
}
