package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
 class OpenAPIScannerPluginTest extends AbstractPluginIT {

     @Test
     void scanNullContract(){
         File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-nulltest.yaml");
         try {
             ContractDescriptor contract = getScanner().scan(file, "example-nulltest.yaml", DefaultScope.NONE);
             assertThat(contract).isNotNull();
         } catch (Exception e){
            fail("Reading contract not containing any data failed", e);
         }
     }

     @Test
     void scanEmptyContract(){  
         File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-emptytest.yaml");
         try {
             ContractDescriptor contract = getScanner().scan(file, "example-emptytest.yaml", DefaultScope.NONE);
             assertThat(contract).isNotNull();
         } catch (Exception e){
             fail("Reading contract only containing container data failed", e);
         }
     }

    @Test
    void scanMetaData(){

        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        ContractDescriptor contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();
        assertThat(contract.getFileName()).isEqualTo("/example-metadata.yaml");

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

    @Test
    void testOperationsFromPaths(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-paths-operations.yaml");
        ContractDescriptor contract = getScanner().scan(testFile, "/example-paths-operations.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();

        List<PathDescriptor> paths = contract.getPaths();

        assertThat(paths).hasSize(11);

        for (PathDescriptor path : paths){
            assertThat(path).isNotNull();
            List<OperationDescriptor> ops = path.getOperations();
            switch (path.getPathUrl()){
                case "/sample":
                    assertThat(ops).hasSize(1);
                    OperationDescriptor op = ops.get(0);
                    assertThat(op.getType()).isEqualTo(OperationDescriptor.HTTPMethod.GET);
                    assertThat(op.getSummary()).isEqualTo("i am a summery!");
                    assertThat(op.getDescription()).isEqualTo("i am a description!");
                    break;
                case "/more_than_one_operaton":
                    assertThat(ops).hasSize(2);
                    break;
                case "/wrong_type":
                    assertThat(ops).hasSize(0);
                    break;
                case "/with_tags":
                    // TBD if tags are implemented
                    break;
                case "/with_depricated":
                    assertThat(ops.get(0).getIsDeprecated()).isEqualTo(true);
                    break;
                case "/without_depricated":
                    assertThat(ops.get(0).getIsDeprecated()).isEqualTo(false);
                    break;
                case "/with_parameter":
                    //assertThat(ops.get(0).getParameters()).hasSize(1);
                    break;
                case "/with_parameters":
                    //assertThat(ops.get(0).getParameters()).hasSize(2);
                    break;
                case "/with_response":
                    assertThat(ops.get(0).getResponses()).hasSize(1);
                    break;
                case "/with_responses":
                    assertThat(ops.get(0).getResponses()).hasSize(2);
                    break;
            }
        }

        store.commitTransaction();

    }
}