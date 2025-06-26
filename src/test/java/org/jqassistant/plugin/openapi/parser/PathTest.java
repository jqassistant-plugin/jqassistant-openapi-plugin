package org.jqassistant.plugin.openapi.parser;

import java.io.File;
import java.util.List;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;

import org.jqassistant.plugin.openapi.api.OpenApiScope;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.PathItemDescriptor;
import org.jqassistant.plugin.openapi.api.model.ServerDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @BeforeEach
    void init(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-path.yaml");
        contract = getScanner().scan(file, "/example-path.yaml", OpenApiScope.CONTRACT);

        store.beginTransaction();

        assertThat(contract).isNotNull();
    }

    @AfterEach
    void closeTransaction(){
        store.commitTransaction();
    }

    @Test
    void basicTest(){
        PathItemDescriptor pathSample = getPathWithUrl("/path_sample");
        assertThat(pathSample.getOperations()).hasSize(1);
        assertThat(pathSample.getOperations().get(0).getExternalDocs()).isNotNull();
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

        assertThat(getPathWithUrl("/path_with_/{t}/w/{o}/parameters").getOperations().get(0).getParameters()).hasSize(1);
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

        PathItemDescriptor singleServerPath = getPathWithUrl("/path_with_a_server");
        assertThat(singleServerPath.getServers()).hasSize(1);

        ServerDescriptor singleServer = singleServerPath.getServers().get(0);
        assertThat(singleServer.getDescription()).isEqualTo("beschreibung server 1");
        assertThat(singleServer.getUrl()).isEqualTo("https://www.1.example.com");
        assertThat(singleServer.getVariables()).isEmpty();

        PathItemDescriptor twoServersPath = getPathWithUrl("/path_with_servers");
        assertThat(twoServersPath.getServers()).hasSize(2);

        assertThat(twoServersPath.getServers()
                .stream()
                .anyMatch(server -> server.getDescription()
                        .equals("beschreibung server 1") && server.getUrl()
                        .equals("https://www.1.example.com") && server.getVariables()
                        .isEmpty())).isTrue();

        assertThat(twoServersPath.getServers()
                .stream()
                .anyMatch(server -> server.getDescription()
                        .equals("beschreibung server 2") && server.getUrl()
                        .equals("https://www.2.example.com") && server.getVariables()
                        .isEmpty())).isTrue();
    }

    @Test
    void propertiesTest(){
        // test reference string
        assertThat(getPathWithUrl("/path_with_ref_string").getRef()).isEqualTo("#/components/pathItems/path_with_ref_string");
        assertThat(getPathWithUrl("/path_with_empty_ref_string")).isNotNull();

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
