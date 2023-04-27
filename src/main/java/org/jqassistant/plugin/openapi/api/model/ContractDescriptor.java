package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.NamedDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

import java.util.List;

/**
 * Represents a OpenAPI Contract
 */
@Label("Contract")
public interface ContractDescriptor extends OpenApiDescriptor, FileDescriptor, NamedDescriptor {
    String getVersion();
    void setVersion(String version);
    String getTitle();
    void setTitle(String title);
    String getDescription();
    void setDescription(String description);
    String getApiVersion();
    void setApiVersion(String apiVersion);
    Object getContact(); // TBD define Contact object
    void setContact(Object contact);

    @Relation("SERVED_BY")
    List<ServerDescriptor> getServers();

    @Relation("HAS_TAG")
    List<TagDescriptor> getTags();

}
