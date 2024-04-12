package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("License")
public interface
LicenseDescriptor extends AsyncApiDescriptor {

    String getName();
    void setName(String name);

    String getIdentifier();
    void setIdentifier(String identifier);

    String getUrl();
    void setUrl(String url);
}
