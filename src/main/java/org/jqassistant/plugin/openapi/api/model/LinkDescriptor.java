package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Link")
public interface LinkDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getOperationRef();
    void setOperationRef(String operationRef);

    String getOperationId();
    void setOperationId(String operationId);

    @Relation("USES_PARAMETER")
    List<LinkParameterDescriptor> getParameters();

    Object getRequestBody();
    void setRequestBody(Object requestBody);

    String getDescription();
    void setDescription(String description);

    @Relation("DEFINES_SERVER")
    ServerDescriptor getServer();
    void setServer(ServerDescriptor server);
}
