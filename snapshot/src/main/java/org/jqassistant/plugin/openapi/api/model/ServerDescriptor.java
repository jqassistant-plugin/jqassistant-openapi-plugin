package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Server")
public interface ServerDescriptor extends OpenApiDescriptor {

    String getUrl();
    void setUrl(String url);

    String getDescription();
    void setDescription(String description);

    @Relation("USES_VARIABLE")
    List<ServerVariableDescriptor> getVariables();
}
