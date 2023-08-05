package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Abstract
@Label("Property")
public interface TypeDescriptor extends JsonSchemaDescriptor {
    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    String getFormat();
    void setFormat(String format);

}