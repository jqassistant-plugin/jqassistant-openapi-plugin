package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class ContractTests extends AbstractPluginIT {

    private final String RESOURCE_DIRECTORY = "/contractTests/";

    private ContractDescriptor parseContract(String filename){
        String filepath = RESOURCE_DIRECTORY + filename;
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), filepath);
        ContractDescriptor contract = getScanner().scan(file, filepath, DefaultScope.NONE);

        if(!store.hasActiveTransaction()) store.beginTransaction();
        assertThat(contract).isNotNull();
        return contract;
    }

    @AfterEach
    public void commitTransaction(){store.commitTransaction();}

    @Test
    void contractWithOnlyOpenApiVersion(){
        ContractDescriptor contract = parseContract("onlyOpenAPIVersion.yaml");

        assertThat(contract.getOpenApiVersion()).isEqualTo("3.1.0");
        assertThat(contract.getInfo()).isNull();
        assertThat(contract.getJsonSchemaDialect()).isNull();
        assertThat(contract.getServers()).hasSize(1); // default server (see https://spec.openapis.org/oas/latest.html#oasServers)
        assertThat(contract.getPaths()).isNull();
        assertThat(contract.getWebhooks()).isEmpty();
        assertThat(contract.getComponents()).isNull();
        assertThat(contract.getSecurity()).isEmpty();
        assertThat(contract.getTags()).isEmpty();
        assertThat(contract.getExternalDocs()).isNull();
    }

    @Test
    void contractWithEmptyProperties(){
        ContractDescriptor contract = parseContract("contractWithEmptyProperties.yaml");

        assertThat(contract.getOpenApiVersion()).isEqualTo("3.1.0");
        assertThat(contract.getInfo()).isNull();
        assertThat(contract.getJsonSchemaDialect()).isNull();
        assertThat(contract.getServers()).hasSize(1); // default server (see https://spec.openapis.org/oas/latest.html#oasServers)
        assertThat(contract.getPaths()).isNull();
        assertThat(contract.getWebhooks()).isEmpty();
        assertThat(contract.getComponents()).isNull();
        assertThat(contract.getSecurity()).isEmpty();
        assertThat(contract.getTags()).isEmpty();
        assertThat(contract.getExternalDocs()).isNull();
    }

    @Test
    void richContract(){
        ContractDescriptor contract = parseContract("richContract.yaml");

        assertThat(contract.getOpenApiVersion()).isEqualTo("3.1.0");
        assertThat(contract.getInfo()).isNotNull();
        assertThat(contract.getJsonSchemaDialect()).isEqualTo("https://spec.openapis.org/oas/3.1/dialect/base");
        assertThat(contract.getServers()).hasSize(2); // default server (see https://spec.openapis.org/oas/latest.html#oasServers)
        assertThat(contract.getPaths()).isNotNull();
        assertThat(contract.getWebhooks()).hasSize(2);
        assertThat(contract.getComponents()).isNotNull();
        assertThat(contract.getSecurity()).hasSize(2);
        assertThat(contract.getTags()).hasSize(2);
        assertThat(contract.getExternalDocs()).isNotNull();
    }
}
