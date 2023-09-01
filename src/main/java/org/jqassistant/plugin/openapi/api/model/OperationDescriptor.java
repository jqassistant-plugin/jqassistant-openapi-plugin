package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Operation")
public interface OperationDescriptor extends OpenApiDescriptor {
    enum HTTPMethod {
        GET, PUT, POST, DELETE, OPTIONS, HEAD, PATCH, TRACE
    }
    HTTPMethod getHttpMethod();
    void setHttpMethod(HTTPMethod httpMethod);

    @Relation("HAS_TAG")
    List<TagDescriptor> getTags();

    String getSummary();
    void setSummary(String summary);

    String getDescription();
    void setDescription(String description);

    @Relation("REFERENCES_EXTERNAL_DOCS")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);

    String getOperationId();
    void setOperationId(String operationId);

    @Relation("USES_PARAMETER")
    List<ParameterDescriptor> getParameters();

    @Relation("DEFINES_REQUEST_BODY")
    RequestBodyDescriptor getRequestBody();
    void setRequestBody(RequestBodyDescriptor requestBody);

    @Relation("DEFINES_RESPONSE")
    List<ResponseDescriptor> getResponses();

    @Relation("DEFINES_CALLBACK")
    List<CallbackDescriptor> getCallbacks();

    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);

    @Relation("DEFINES_SECURITY")
    List<SecurityRequirementDescriptor> getSecurityRequirements();

    @Relation("DEFINES_SERVER")
    List<ServerDescriptor> getServers();






}
