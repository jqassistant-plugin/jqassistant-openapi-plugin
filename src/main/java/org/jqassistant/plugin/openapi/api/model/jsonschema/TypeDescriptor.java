package org.jqassistant.plugin.openapi.api.model.jsonschema;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;

@Abstract
@Label("Type")
public interface TypeDescriptor extends JsonSchemaDescriptor {

    String getFormat();
    void setFormat(String format);

}