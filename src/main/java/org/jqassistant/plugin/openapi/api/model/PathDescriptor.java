package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Path")
public interface PathDescriptor extends OpenApiDescriptor{

    String get$ref();
    void set$ref(String referenceString);

    String getSummary();
    void setSummary(String summary);

    String getDescription();
    void setDescription(String description);

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();

    @Relation("ACCEPTS")
    List<ParameterDescriptor> getParameters();

    @Relation("PROVIDES")
    List<OperationDescriptor> getOperations();

}
