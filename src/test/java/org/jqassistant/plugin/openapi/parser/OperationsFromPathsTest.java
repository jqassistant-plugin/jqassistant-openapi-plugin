package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.OperationDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathItemDescriptor;
import org.jqassistant.plugin.openapi.api.model.TagDescriptor;
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
        assertThat(paths).hasSize(13);
    }
    
    @Test
    void basicTest(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/sample");

        assertThat(ops).hasSize(1);
        OperationDescriptor op = ops.get(0);
        assertThat(op.getType()).isEqualTo(OperationDescriptor.HTTPMethod.GET);
        assertThat(op.getSummary()).isEqualTo("i am a summary!");
        assertThat(op.getDescription()).isEqualTo("i am a description!");
    }

    @Test
    void moreThanOneOperation(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/more_than_one_operaton");
        assertThat(ops).hasSize(2);
    }

    @Test
    void wrongTypesGetFiltered(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/wrong_type");
        assertThat(ops).isEmpty();
    }

    @Test
    void hasTags(){
        List<OperationDescriptor> opsWithTags = getPathOpsWithUrl("/with_tags");
        assertThat(opsWithTags).hasSize(1);

        OperationDescriptor opWithTags = opsWithTags.get(0);
        assertThat(opWithTags.getTags()).hasSize(2);

        TagDescriptor tag1 = opWithTags.getTags().get(0);
        assertThat(tag1.getTag()).isEqualTo("tag1");
        assertThat(tag1.getDescription()).isNull();
        assertThat(tag1.getExternalDocs()).isNull();

        TagDescriptor tag2 = opWithTags.getTags().get(1);
        assertThat(tag2.getTag()).isEqualTo("tag2");
        assertThat(tag2.getDescription()).isNull();
        assertThat(tag2.getExternalDocs()).isNull();

        List<OperationDescriptor> opsEmptyTags = getPathOpsWithUrl("/with_empty_tags");
        assertThat(opsEmptyTags).hasSize(1);
        assertThat(opsEmptyTags.get(0).getTags()).isEmpty();

        List<OperationDescriptor> opsNoTags = getPathOpsWithUrl("/with_no_tags");
        assertThat(opsNoTags).hasSize(1);
        assertThat(opsNoTags.get(0).getTags()).isEmpty();
    }

    @Test
    void depricatedFlag(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/with_depricated");
        assertThat(ops.get(0).getIsDeprecated()).isTrue();
    }

    @Test
    void depricatedFlagDefaultValue(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/without_depricated");
        assertThat(ops.get(0).getIsDeprecated()).isFalse();
    }

    @Test
    void oneParameter(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/with_parameter");

        // TBD
    }

    @Test
    void multibeParameter(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/with_parameters");

        // TBD
    }

    @Test
    void oneResponse(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/with_response");
        assertThat(ops.get(0).getResponses()).hasSize(1);
    }

    @Test
    void multibleResponses(){
        List<OperationDescriptor> ops = getPathOpsWithUrl("/with_responses");
        assertThat(ops.get(0).getResponses()).hasSize(2);
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
