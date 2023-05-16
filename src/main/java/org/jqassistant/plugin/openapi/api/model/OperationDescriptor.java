package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.ArrayList;
import java.util.List;

@Label("Operation")
public interface OperationDescriptor extends OpenApiDescriptor{
    enum HTTPOperationType {
        GET, PUT, POST, DELETE, OPTIONS, HEAD, PATCH, TRACE
    }

    HTTPOperationType getType();
    void setType(HTTPOperationType type);

    String getSummary();
    void setSummary(String summary);

    String getDescription();
    void setDescription(String description);

    String getOperationId();
    void setOperationId(String operationId);

    Boolean getIsDeprecated();
    void setIsDeprecated(Boolean isDeprecated);

    // TODO (TBD) implement tags

    // TODO (TBD) implement Security object

    // TODO (TBD) implement ExternalDocs object

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
