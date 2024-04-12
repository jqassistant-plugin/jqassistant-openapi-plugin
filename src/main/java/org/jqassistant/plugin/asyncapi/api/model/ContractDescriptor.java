package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;


@Label("Contract")
public interface ContractDescriptor extends AsyncApiDescriptor, FileDescriptor {
    String getAsyncApiVersion();
    void setAsyncApiVersion(String asyncApiVersion);

    @Relation("HOLDS_INFO")
    InfoDescriptor getInfo();
    void setInfo(InfoDescriptor info);

}
