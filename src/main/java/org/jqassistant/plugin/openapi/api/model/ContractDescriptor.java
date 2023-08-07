package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

/**
 * Represents a OpenAPI Contract
 */
@Label("Contract")
public interface ContractDescriptor extends OpenApiDescriptor, FileDescriptor {
    String getOpenApiVersion();
    void setOpenApiVersion(String openApiVersion);

    @Relation("HOLDS_INFORMATION")
    InfoDescriptor getInfo();
    void setInfo(InfoDescriptor info);

    @Relation("HAS_CONTACT")
    ContactDescriptor getContact();
    void setContact(ContactDescriptor contact);

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();

    @Relation("HAS_TAG")
    List<TagDescriptor> getTags();

    @Relation("DEFINES_PATHS")
    PathsDescriptor getPaths();
    void setPaths(PathsDescriptor pathsDescriptor);

    @Relation("DEFINES_COMPONENTS")
    ComponentsDescriptor getComponents();
    void setComponents(ComponentsDescriptor components);

    @Relation("REFERENCES")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);
}
