package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathItemDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @BeforeEach
    void init(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-path.yaml");
        contract = getScanner().scan(file, "/example-path.yaml", DefaultScope.NONE);

        store.beginTransaction();

        assertThat(contract).isNotNull();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void basicTest(){
        assertThat(getPathWithUrl("/path_sample").getOperations()).hasSize(1);
    }


    @Test
    void subPathTest(){
        assertThat(getPathWithUrl("/path_sample/sub_path").getOperations()).hasSize(1);
    }

    @Test
    void realWorldExampleTest(){
        assertThat(getPathWithUrl("/example_from_sawgger_io").getOperations()).hasSize(1);
    }

    @Test
    void parameterTest(){
        assertThat(getPathWithUrl("/path_with_no/parameter").getParameters()).isEmpty();
        assertThat(getPathWithUrl("/path_with_a_/{single}/parameter").getParameters()).hasSize(1);
        assertThat(getPathWithUrl("/path_with_/{t}/w/{o}/parameters").getParameters()).hasSize(2);
    }

    @Test
    void multibeOperationsTest(){
        assertThat(getPathWithUrl("/path_with_two_ops").getOperations()).hasSize(2);
    }

    @Test
    void emptyPathTest(){
        assertThat(getPathWithUrl("path_with_nothing")).isNull();
    }

    @Test
    void serversTest(){
        assertThat(getPathWithUrl("/path_with_no_server").getServers()).isEmpty();
        assertThat(getPathWithUrl("/path_with_a_server").getServers()).hasSize(1);
        assertThat(getPathWithUrl("/path_with_servers").getServers()).hasSize(2);
    }

    @Test
    void propertiesTest(){
        // test reference string
        assertThat(getPathWithUrl("/path_with_ref_string").getReferenceString()).isEqualTo("#/components/pathItems/path_with_ref_string");
        assertThat(getPathWithUrl("/path_with_empty_ref_string")).isNull();

        // test description
        assertThat(getPathWithUrl("/path_with_no_description").getDescription()).isNull();
        assertThat(getPathWithUrl("/path_with_empty_description").getDescription()).isNull();
        assertThat(getPathWithUrl("/path_with_a_description").getDescription()).isEqualTo("This is a sample path with a description");

        // test summary
        assertThat(getPathWithUrl("/path_with_empty_summary").getSummary()).isNull();
        assertThat(getPathWithUrl("/path_with_no_summary").getSummary()).isNull();
        assertThat(getPathWithUrl("/path_with_a_summary").getSummary()).isEqualTo("This is a extremely short summary.");

    }

    private PathItemDescriptor getPathWithUrl(String url){
        List<PathItemDescriptor> pathItems = contract.getPaths().getPathItems();
        assertThat(pathItems).hasSize(18);      //check if all valid paths are present


        for (PathItemDescriptor path : pathItems){
            assertThat(path).isNotNull();
            if (path.getPathUrl().equals(url))
                return path;
        }

        return null;
    }

}
