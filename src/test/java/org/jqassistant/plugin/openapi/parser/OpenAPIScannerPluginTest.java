package org.jqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.scanner.api.DefaultScope;
import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import org.jqassistant.plugin.openapi.api.model.*;
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

        assertThat(contract.getApiVersion()).isEqualTo("1.0.0");
        assertThat(contract.getTitle()).isEqualTo("Issues");
        assertThat(contract.getDescription()).isEqualTo("Issues API");

        assertThat(contract.getServers()).hasSize(1);
        ServerDescriptor server = contract.getServers().get(0);
        assertThat(server.getUrl()).isEqualTo("/rest/v1/users");

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

    private TagDescriptor getTagByName(String name){
        List<TagDescriptor> tags = contract.getTags();
        for(TagDescriptor tag: tags)
            if(tag.getTag().equals(name))
                return tag;
        return null;
    }
}