package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Operation")
public interface OperationDescriptor extends OpenApiDescriptor, DescriptionTemplate {
    enum HTTPMethod {
        GET, PUT, POST, DELETE, OPTIONS, HEAD, PATCH, TRACE
    }

    @Relation("HAS")
    List<TagDescriptor> getTags();

    HTTPMethod getType();
    void setType(HTTPMethod type);
    String getSummary();
    void setSummary(String summary);
    String getOperationId();
    void setOperationId(String operationId);
    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);

    @Relation("REFERENCES")
    ExternalDocsDescriptor getExternalDocs();
    void setExternalDocs(ExternalDocsDescriptor externalDocs);

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();

    @Relation("ACCEPTS")
    List<ParameterDescriptor> getParameters();

    @Relation("EXPECTS")
    RequestBodyDescriptor getRequestBody();
    void setRequestBody(RequestBodyDescriptor requestBody);

    @Relation("RETURNS")
    List<ResponseDescriptor> getResponses();
}
