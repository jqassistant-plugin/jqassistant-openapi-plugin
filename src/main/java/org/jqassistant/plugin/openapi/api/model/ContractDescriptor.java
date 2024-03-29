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

    @Relation("HOLDS_INFO")
    InfoDescriptor getInfo();
    void setInfo(InfoDescriptor info);

    String getJsonSchemaDialect();
    void setJsonSchemaDialect(String jsonSchemaDialect);

    @Relation("DEFINES_SERVER")
    List<ServerDescriptor> getServers();

    @Relation("DEFINES_PATHS")
    PathsDescriptor getPaths();
    void setPaths(PathsDescriptor pathsDescriptor);

    @Relation("DEFINES_WEBHOOK")
    List<PathItemDescriptor> getWebhooks();

    @Relation("DEFINES_COMPONENTS")
    ComponentsDescriptor getComponents();
    void setComponents(ComponentsDescriptor components);

    @Relation("DEFINES_SECURITY")
    List<SecurityRequirementDescriptor> getSecurity();

    @Relation("HAS_TAG")
    List<TagDescriptor> getTags();

    @Relation("REFERENCES_EXTERNAL_DOCS")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);
}
