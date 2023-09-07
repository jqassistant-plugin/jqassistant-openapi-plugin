package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("PathItem")
public interface PathItemDescriptor extends OpenApiDescriptor, DescriptionTemplate {
    String getPathUrl();
    void setPathUrl(String pathUrl);
    String getRef();
    void setRef(String ref);
    String getSummary();
    void setSummary(String summary);

    @Relation("DEFINES_SERVER")
    List<ServerDescriptor> getServers();

    @Relation("USES_PARAMETER")
    List<ParameterDescriptor> getParameters();

    @Relation("DEFINES_OPERATION")
    List<OperationDescriptor> getOperations();

}
