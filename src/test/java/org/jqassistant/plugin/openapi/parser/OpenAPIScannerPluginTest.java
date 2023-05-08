package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.*;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.*;
 class OpenAPIScannerPluginTest extends AbstractPluginIT {

    @Test
    void scanYAMLFile(){

        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        ContractDescriptor contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();
        assertThat(contract.getFileName()).isEqualTo("example-metadata.yaml");

        assertThat(contract.getApiVersion()).isEqualTo("1.0.0");
        assertThat(contract.getTitle()).isEqualTo("Issues");
        assertThat(contract.getDescription()).isEqualTo("Issues API");

        assertThat(contract.getServers()).hasSize(1);
        ServerDescriptor server = contract.getServers().get(0);
        assertThat(server.getUrl()).isEqualTo("/rest/v1/users");

        assertThat(contract.getTags()).hasSize(1);
        TagDescriptor tag = contract.getTags().get(0);
        assertThat(tag.getTag()).isEqualTo("issues");
        assertThat(tag.getDescription()).isEqualTo("Issues API");

        store.commitTransaction();
    }
}