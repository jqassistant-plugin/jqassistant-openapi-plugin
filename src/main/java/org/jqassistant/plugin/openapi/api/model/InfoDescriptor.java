package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("Info")
public interface InfoDescriptor extends OpenApiDescriptor{

    String getTitle();
    void setTitle(String title);

    String getSummary();
    void setSummary(String summary);

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

    String getVersion();
    void setVersion(String version);
}
