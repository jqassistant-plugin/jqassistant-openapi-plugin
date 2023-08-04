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

    @Relation("REFERENCES")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);

    String getOperationId();
    void setOperationId(String operationId);

    @Relation("ACCEPTS")
    List<ParameterDescriptor> getParameters();

    @Relation("EXPECTS")
    RequestBodyDescriptor getRequestBody();
    void setRequestBody(RequestBodyDescriptor requestBody);

    @Relation("RETURNS")
    List<ResponseDescriptor> getResponses();

    @Relation("HAS_CALLBACK")
    List<CallbackDescriptor> getCallbacks();

    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);

    @Relation("DECLARES")
    SecurityRequirementDescriptor getSecurityRequirement();
    void setSecurityRequirement(SecurityRequirementDescriptor securityRequirement);

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();






}
