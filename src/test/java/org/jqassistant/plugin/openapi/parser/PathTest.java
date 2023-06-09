package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest extends AbstractPluginIT {

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
    void oneParameterTest(){
        assertThat(getPathWithUrl("/path_with_a_/{single}/parameter").getOperations()).hasSize(1);
    }

    @Test
    void multibleParameterTest(){
        assertThat(getPathWithUrl("/path_with_/{t}/w/{o}/parameters").getOperations()).hasSize(1);
    }

    @Test
    void multibeOperationsTest(){
        assertThat(getPathWithUrl("/path_with_two_ops").getOperations()).hasSize(2);
    }

    @Test
    void emptyPathTest(){
        assertThat(getPathWithUrl("path_with_nothing")).isNull();
    }

    private PathDescriptor getPathWithUrl(String url){
        List<PathDescriptor> paths = contract.getPaths();
        assertThat(paths).hasSize(6);      //check if all valid paths are present


        for (PathDescriptor path : paths){
            assertThat(path).isNotNull();
            if (path.getPathUrl().equals(url))
                return path;
        }

        return null;
    }

}
