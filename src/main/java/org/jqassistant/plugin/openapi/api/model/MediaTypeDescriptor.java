package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;

import java.util.List;

@Label("MediaType")
public interface MediaTypeDescriptor extends OpenApiDescriptor{
    String getMediaType();
    void setMediaType(String mediaType);

    @Relation("DEFINED_BY")
    SchemaDescriptor getSchema();
    void setSchema(SchemaDescriptor schema);

    Object getExample();
    void setExample(Object example);

    @Relation("PROVIDES_EXAMPLE")
    List<ExampleDescriptor> getExamples();

    @Relation("USES_ENCODING")
    List<EncodingDescriptor> getEncodings();
}
