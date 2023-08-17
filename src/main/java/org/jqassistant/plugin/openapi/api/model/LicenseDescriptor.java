package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("License")
public interface LicenseDescriptor extends OpenApiDescriptor{

    String getName();
    void setName(String name);

    String getIdentifier();
    void setIdentifier(String identifier);

    String getUrl();
    void setUrl(String url);
}
