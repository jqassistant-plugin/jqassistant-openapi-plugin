package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
 class OpenAPIScannerPluginTest extends AbstractPluginIT {

    ContractDescriptor contract;

    @Test
    void scanNullContract(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-nulltest.yaml");
        try {
            contract = getScanner().scan(file, "example-nulltest.yaml", DefaultScope.NONE);
            assertThat(contract).isNotNull();
        } catch (Exception e){
            fail("Reading contract not containing any data failed", e);
        }
    }
  
    @Test
    void scanEmptyContract(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-emptytest.yaml");
        try {
            contract = getScanner().scan(file, "example-emptytest.yaml", DefaultScope.NONE);
            assertThat(contract).isNotNull();
        } catch (Exception e){
            fail("Reading contract only containing container data failed", e);
        }
    }


    @Test
    void scanMetaData(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();
        assertThat(contract.getFileName()).isEqualTo("/example-metadata.yaml");

        assertThat(contract.getServers()).hasSize(2);

        assertThat(contract.getTags()).hasSize(7);
        TagDescriptor tag = getTagByName("issues");
        assertThat(tag.getTag()).isEqualTo("issues");
        assertThat(tag.getDescription()).isEqualTo("Issues API");

        assertThat(contract.getExternalDocs()).isNotNull();
        assertThat(contract.getExternalDocs().getDescription()).isNotNull();
        assertThat(contract.getExternalDocs().getUrl()).isNotNull();

        store.commitTransaction();
    }

    @Test
    void scanRichInfo(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract.getInfo()).isNotNull();
        InfoDescriptor info = contract.getInfo();
        assertThat(info.getTitle()).isEqualTo("Issues");
        assertThat(info.getSummary()).isEqualTo("summary");
        assertThat(info.getDescription()).isEqualTo("Issues API");
        assertThat(info.getTermsOfService()).isEqualTo("TOS");
        assertThat(info.getContact()).isNotNull();
        assertThat(info.getContact().getEmail()).isEqualTo("email@example.com");
        assertThat(info.getContact().getName()).isEqualTo("Example Name");
        assertThat(info.getContact().getUrl()).isEqualTo("contact.example.com");
        assertThat(info.getLicense()).isNotNull();
        assertThat(info.getVersion()).isEqualTo("1.0.0");
        store.commitTransaction();
    }

    @Test
    void scanLicenseWithUrl(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract.getInfo()).isNotNull();
        InfoDescriptor info = contract.getInfo();
        assertThat(info.getLicense()).isNotNull();
        assertThat(info.getLicense().getUrl()).isEqualTo("licenseUrl");
        assertThat(info.getLicense().getName()).isEqualTo("License Name");
        // assertion for info.getLicense().getIdentifier() not needed as mutually exclusive to license.url
        store.commitTransaction();
    }

     @Test
     void scanLicenseWithIdentifier(){
         File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-components.yaml");
         contract = getScanner().scan(testFile, "/example-components.yaml", DefaultScope.NONE);

         store.beginTransaction();
         assertThat(contract.getInfo()).isNotNull();
         InfoDescriptor info = contract.getInfo();
         assertThat(info.getLicense()).isNotNull();
         // assertion for info.getLicense().getUrl() not needed as mutually exclusive to license.identifier
         assertThat(info.getLicense().getName()).isEqualTo("License Name");
         assertThat(info.getLicense().getIdentifier()).isEqualTo("id123");
         store.commitTransaction();
     }

     @Test
     void scanEmptyInfo(){
         File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-emptytest.yaml");
         contract = getScanner().scan(testFile, "example-emptytest.yaml", DefaultScope.NONE);

         store.beginTransaction();
         assertThat(contract.getInfo()).isNotNull();
         InfoDescriptor info = contract.getInfo();
         assertThat(info.getTitle()).isNull();
         assertThat(info.getSummary()).isNull();
         assertThat(info.getDescription()).isNull();
         assertThat(info.getTermsOfService()).isNull();
         assertThat(info.getContact()).isNull();
         assertThat(info.getLicense()).isNull();
         assertThat(info.getVersion()).isNull();
         store.commitTransaction();
     }

     @Test
     void scanEmptyLicense(){
         File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-emptyinfo.yaml");
         contract = getScanner().scan(testFile, "example-emptyinfo.yaml", DefaultScope.NONE);

         store.beginTransaction();
         assertThat(contract.getInfo()).isNotNull();
         LicenseDescriptor license = contract.getInfo().getLicense();
         assertThat(license.getIdentifier()).isNull();
         assertThat(license.getName()).isNull();
         assertThat(license.getUrl()).isNull();
         store.commitTransaction();
     }

     @Test
     void scanEmptyContact(){
         File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-emptyinfo.yaml");
         contract = getScanner().scan(testFile, "example-emptyinfo.yaml", DefaultScope.NONE);

         store.beginTransaction();
         assertThat(contract.getInfo()).isNotNull();
         ContactDescriptor contactDescriptor = contract.getInfo().getContact();
         assertThat(contactDescriptor.getEmail()).isNull();
         assertThat(contactDescriptor.getName()).isNull();
         assertThat(contactDescriptor.getUrl()).isNull();
         store.commitTransaction();
     }

    @Test
    void scanTags(){
        File testFile = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        contract = getScanner().scan(testFile, "/example-metadata.yaml", DefaultScope.NONE);

        store.beginTransaction();
        assertThat(contract).isNotNull();
        assertThat(contract.getTags()).hasSize(7);

        TagDescriptor tagAllFields = getTagByName("tag_with_all_fields");
        assertThat(tagAllFields.getTag()).isEqualTo("tag_with_all_fields");
        assertThat(tagAllFields.getDescription()).isEqualTo("This is a tag with all fields set");
        assertThat(tagAllFields.getExternalDocs()).isNotNull();

        TagDescriptor tagEmptyFields = getTagByName("tag_with_empty_fields");
        assertThat(tagEmptyFields.getTag()).isEqualTo("tag_with_empty_fields");
        assertThat(tagEmptyFields.getDescription()).isNull();
        assertThat(tagEmptyFields.getExternalDocs()).isNull();

        TagDescriptor tagNoFields = getTagByName("tag_with_no_fields");
        assertThat(tagNoFields.getTag()).isEqualTo("tag_with_no_fields");
        assertThat(tagNoFields.getDescription()).isNull();
        assertThat(tagNoFields.getExternalDocs()).isNull();

        store.commitTransaction();
    }

    @Test
    void scanWholeContract(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example.yaml");
        try {
            ContractDescriptor contract = getScanner().scan(file, "example.yaml", DefaultScope.NONE);
            assertThat(contract).isNotNull();
        } catch (Exception e){
            fail("Reading whole example contract failed", e);
        }
    }

    @Test
    void testExternalDocs(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        try {
            contract = getScanner().scan(file, "example-metadata.yaml", DefaultScope.NONE);
            assertThat(contract).isNotNull();
        } catch (Exception e){
            fail("Reading contract only containing container data failed", e);
        }

        store.beginTransaction();
        ExternalDocsDescriptor externalDocsWithProperties = getTagByName("issues").getExternalDocs();
        assertThat(externalDocsWithProperties.getDescription()).isEqualTo("issues - external docs description");
        assertThat(externalDocsWithProperties.getUrl()).isEqualTo("https://www.1.example.com");

        ExternalDocsDescriptor externalDocsWithoutDescription = getTagByName("externalDocs_without_description").getExternalDocs();
        assertThat(externalDocsWithoutDescription.getDescription()).isNull();
        assertThat(externalDocsWithoutDescription.getUrl()).isEqualTo("https://www.2.example.com");

        ExternalDocsDescriptor externalDocsWithoutUrl = getTagByName("externalDocs_without_url").getExternalDocs();
        assertThat(externalDocsWithoutUrl.getDescription()).isEqualTo("external docs without url");
        assertThat(externalDocsWithoutUrl.getUrl()).isNull();

        ExternalDocsDescriptor externalDocsWithEmptyProps = getTagByName("externalDocs_with_empty_props").getExternalDocs();
        assertThat(externalDocsWithEmptyProps.getDescription()).isNull();
        assertThat(externalDocsWithEmptyProps.getUrl()).isNull();

        store.commitTransaction();
    }

    @Test
    void testServerVariable(){
        File file = new File(getClassesDirectory(OpenAPIScannerPluginTest.class), "example-metadata.yaml");
        try {
            contract = getScanner().scan(file, "example-metadata.yaml", DefaultScope.NONE);
            assertThat(contract).isNotNull();
        } catch (Exception e){
            fail("Reading contract only containing container data failed", e);
        }

        store.beginTransaction();

        final String serverUrl = "/url/with/{first_variable}/and/{second_variable}/{no_fields_variable}";

        ServerDescriptor varTestServer = getServerByUrl(serverUrl);
        assertThat(varTestServer.getVariables()).hasSize(3);

        ServerVariableDescriptor firstVar = getServerVarByName(serverUrl, "first_variable");
        final String[] expectedPossibleValue = {"value1", "value2"};
        Assertions.assertArrayEquals(expectedPossibleValue, firstVar.getPossibleValues());
        assertThat(firstVar.getDefault()).isEqualTo("value3");
        assertThat(firstVar.getDescription()).isEqualTo("variable description");

        ServerVariableDescriptor secondVar = getServerVarByName(serverUrl, "second_variable");
        assertThat(secondVar.getPossibleValues()).isNull();
        assertThat(secondVar.getDefault()).isNull();
        assertThat(secondVar.getDescription()).isNull();

        ServerVariableDescriptor noFieldsVar = getServerVarByName(serverUrl, "no_fields_variable");
        assertThat(noFieldsVar.getPossibleValues()).isNull();
        assertThat(noFieldsVar.getDefault()).isEqualTo("no fields default");
        assertThat(noFieldsVar.getDescription()).isNull();

        store.commitTransaction();
    }

    private TagDescriptor getTagByName(String name){
        List<TagDescriptor> tags = contract.getTags();
            for(TagDescriptor tag: tags)
                if(tag.getTag().equals(name))
                    return tag;
        return null;
    }

    private ServerDescriptor getServerByUrl(String url){
        List<ServerDescriptor> servers = contract.getServers();
            for(ServerDescriptor server: servers)
                if(server.getUrl().equals(url))
                    return server;
        return null;
    }

    private ServerVariableDescriptor getServerVarByName(String serverUrl, String name){
        List<ServerVariableDescriptor> variables = getServerByUrl(serverUrl).getVariables();
        for(ServerVariableDescriptor serverVar: variables)
            if(serverVar.getName().equals(name))
                return serverVar;
        fail("no serverVariable in path <%s> with name <%s>", serverUrl, name);
        return null;
    }

}