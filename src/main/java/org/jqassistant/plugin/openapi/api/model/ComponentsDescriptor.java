package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;

import java.util.List;
@Label("Components")
public interface ComponentsDescriptor extends OpenApiDescriptor{

    @Relation("DEFINES_REQUEST_BODY")
    List<RequestBodyDescriptor> getRequestBodies();

    @Relation("DEFINES_HEADER")
    List<HeaderDescriptor> getHeaders();

    @Relation("DEFINES_SECURITY_SCHEME")
    List<SecuritySchemeDescriptor> getSecuritySchemes();

    @Relation("DEFINES_LINK")
    List<LinkDescriptor> getLinks();

    @Relation("DEFINES_CALLBACK")
    List<CallbackDescriptor> getCallBacks();

    @Relation("DEFINES_PATH_ITEM")
    List<PathItemDescriptor> getPathItems();

    @Relation("DEFINES_EXAMPLE")
    List<ExampleDescriptor> getExamples();

    @Relation("DEFINES_RESPONSE")
    List<ResponseDescriptor> getResponses();

    @Relation("DEFINES_PARAMETER")
    List<ParameterDescriptor> getParameters();

    @Relation("DEFINES_SCHEMA")
    List<SchemaDescriptor> getSchemas();


}
