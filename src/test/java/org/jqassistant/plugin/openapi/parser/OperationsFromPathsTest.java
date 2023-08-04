package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.OperationDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathItemDescriptor;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class OperationsFromPathsTest extends AbstractPluginIT {

    ContractDescriptor contract;
    List<PathItemDescriptor> paths;

    @BeforeEach
    void init(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-paths-operations.yaml");
        contract = getScanner().scan(testFile, "/example-paths-operations.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();

        paths = contract.getPaths().getPathItems();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void allPathsPresent(){
        assertThat(paths).hasSize(3);
    }

    @Test
    void richOperation(){
        OperationDescriptor richOp = getPathOpsWithUrl("/richOperation").get(0);
        assertThat(richOp.getHttpMethod()).isEqualTo(OperationDescriptor.HTTPMethod.GET);
        assertThat(richOp.getTags()).hasSize(2);
        assertThat(richOp.getSummary()).isEqualTo("operation with all fields populated");
        assertThat(richOp.getDescription()).isEqualTo("same as summary");
        assertThat(richOp.getExternalDocs()).isNotNull();
        assertThat(richOp.getOperationId()).isEqualTo("operationId");
        assertThat(richOp.getParameters()).hasSize(2);
        assertThat(richOp.getRequestBody()).isNotNull();
        assertThat(richOp.getResponses()).hasSize(2);
        assertThat(richOp.getCallbacks()).hasSize(1);
        assertThat(richOp.getIsDeprecated()).isTrue();
        assertThat(richOp.getSecurityRequirements()).hasSize(1);
        assertThat(richOp.getServers()).hasSize(3);
    }

    @Test
    void emptyOperation(){
        OperationDescriptor emptyOp = getPathOpsWithUrl("/emptyOperation").get(0);
        assertThat(emptyOp.getHttpMethod()).isEqualTo(OperationDescriptor.HTTPMethod.GET);
        assertThat(emptyOp.getTags()).isEmpty();
        assertThat(emptyOp.getSummary()).isNull();
        assertThat(emptyOp.getDescription()).isNull();
        assertThat(emptyOp.getExternalDocs()).isNull();
        assertThat(emptyOp.getOperationId()).isNull();
        assertThat(emptyOp.getParameters()).isEmpty();
        assertThat(emptyOp.getRequestBody()).isNull();
        assertThat(emptyOp.getResponses()).isEmpty();
        assertThat(emptyOp.getCallbacks()).isEmpty();
        assertThat(emptyOp.getIsDeprecated()).isNull();
        assertThat(emptyOp.getSecurityRequirements()).isEmpty();
        assertThat(emptyOp.getServers()).isEmpty();
    }

    @Test
    void noFieldsOperation(){
        OperationDescriptor noFieldsOp = getPathOpsWithUrl("/noFieldsOperation").get(0);
        assertThat(noFieldsOp.getHttpMethod()).isEqualTo(OperationDescriptor.HTTPMethod.GET);
        assertThat(noFieldsOp.getTags()).isEmpty();
        assertThat(noFieldsOp.getSummary()).isNull();
        assertThat(noFieldsOp.getDescription()).isNull();
        assertThat(noFieldsOp.getExternalDocs()).isNull();
        assertThat(noFieldsOp.getOperationId()).isEqualTo("no fields operation");
        assertThat(noFieldsOp.getParameters()).isEmpty();
        assertThat(noFieldsOp.getRequestBody()).isNull();
        assertThat(noFieldsOp.getResponses()).isEmpty();
        assertThat(noFieldsOp.getCallbacks()).isEmpty();
        assertThat(noFieldsOp.getIsDeprecated()).isNull();
        assertThat(noFieldsOp.getSecurityRequirements()).isEmpty();
        assertThat(noFieldsOp.getServers()).isEmpty();
    }
    
    private List<OperationDescriptor> getPathOpsWithUrl(String url) {
        for (PathItemDescriptor path : paths) {
            assertThat(path).isNotNull();
            if (path.getPathUrl().equals(url))
                return path.getOperations();
        }
        
        fail("No such Path");
        return null;
    }
}
