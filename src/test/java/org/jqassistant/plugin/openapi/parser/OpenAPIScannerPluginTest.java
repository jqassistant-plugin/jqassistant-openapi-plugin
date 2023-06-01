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
     void pathTest(){
         File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-path.yaml");
         ContractDescriptor contract = getScanner().scan(file, "/example-path.yaml", DefaultScope.NONE);

         store.beginTransaction();

         assertThat(contract).isNotNull();

         List<PathDescriptor> paths = contract.getPaths();
         assertThat(paths).hasSize(6);      //check if all valid paths are present

         for (PathDescriptor path : paths){
             assertThat(path).isNotNull();
             switch (path.getPathUrl()){
                 // mostly checking for existence of path, more detailed test will be done in different tests.
                 case "/path_sample":               // Sanity check
                 case "/path_sample/sub_path":      // check sub-paths
                 case "/example_from_sawgger_io":   // real world example from swagger.io
                 case "/path_with_a_/{single}/parameter":   // path with single parameter
                 case "/path_with_/{t}/w/{o}/parameters":    // path with more than one parameter
                     assertThat(path.getOperations()).hasSize(1);
                     break;
                 case "/path_with_two_ops":         // path with more than one operation
                     assertThat(path.getOperations()).hasSize(2);
                     break;
                 case "/path_with_nothing":         // empty path entry
                     fail("contains empty attribute");
                     break;
                 default:
                     fail("not expected path found! (" + path.getPathUrl() + ")");
             }
         }

         store.commitTransaction();
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
}