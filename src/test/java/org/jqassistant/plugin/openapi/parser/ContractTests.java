package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.junit.jupiter.api.AfterEach;

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
}
