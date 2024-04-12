package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;


@Label("Info")
public interface InfoDescriptor extends AsyncApiDescriptor {

    String getTitle();
    void setTitle(String title);

    String getVersion();
    void setVersion(String version);

    String getDescription();
    void setDescription(String description);

    String getTermsOfService();
    void setTermsOfService(String termsOfService);

    @Relation("INCLUDES_CONTACT")
    ContactDescriptor getContact();
    void setContact(ContactDescriptor contact);

    @Relation("INCLUDES_LICENSE")
    LicenseDescriptor getLicense();
    void setLicense(LicenseDescriptor license);

    @Relation("HAS_TAG")
    List<TagDescriptor> getTags();

    @Relation("INCLUDES_DOCUMENTATION")
    ExternalDocsDescriptor getExternalDocumentation();
    void setExternalDocumentation (ExternalDocsDescriptor externalDocumentation);

    // TODO: Specification Extensions handeln (external documentation)
}