package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

enum HTTPOperationType {
    GET,
    PUT,
    POST,
    DELETE,
    OPTIONS,
    HEAD,
    PATCH,
    TRACE

}
public interface OperationDescriptor extends OpenApiDescriptor{
    //, deprecated, security

    HTTPOperationType getType();
    void setType(HTTPOperationType type);

    List<String> getTags();
    void setTags(List<String> tags);

    String getSummary();
    void setSummary(String summary);

    String getDescription();
    void setDescription(String description);

    String getOperationId();
    void setOperationId(String operationId);

    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);

    //TODO (TBD) implement Security object

    //TODO (TBD) implement ExternalDocs object

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();

    @Relation("ACCEPTS")
    List<ParameterDescriptor> getParameters();

    @Relation("EXPECTS")
    RequestBodyDescriptor getRequestBody();

    @Relation("RETURNS")
    List<ResponseDescriptor> getResponses();

}
