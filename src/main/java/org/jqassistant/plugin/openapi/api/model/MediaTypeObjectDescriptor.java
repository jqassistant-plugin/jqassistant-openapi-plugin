package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Relation;


public interface MediaTypeObjectDescriptor extends OpenApiDescriptor{

    String getMediaType();
    void setMediaType(String mediaType);

    // TODO (TBD) implement examples property

    // TODO (TBD) implement encoding property

    /* TODO (TBD) implement SchemaDescriptor
     * @Relation("DEFINED_BY")
     * SchemaDescriptor getSchema();
     */

}
