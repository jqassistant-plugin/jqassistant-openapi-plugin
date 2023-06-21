package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

@Label("Component")
public interface ComponentDescriptor extends OpenApiDescriptor {

    @Relation("INCLUDES")
    List<RequestBodyDescriptor> getRequestBodies();

    @Relation("INCLUDES")
    List<HeaderDescriptor> getHeaders();

    @Relation("INCLUDES")
    List<SecuritySchemaDescriptor> getSecuritySchemas();

    @Relation("INCLUDES")
    List<LinkDescriptor> getLinks();

    @Relation("INCLUDES")
    List<CallbackDescriptor> getCallBacks();

    @Relation("INCLUDES")
    List<PathItemDescriptor> getPathItems();

    @Relation("INCLUDES")
    List<ExampleDescriptor> getExamples();

    @Relation("INCLUDES")
    List<ResponseDescriptor> getResponses();

    @Relation("INCLUDES")
    List<ParameterDescriptor> getParameters();


}
