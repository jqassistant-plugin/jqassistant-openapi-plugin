package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Contact")
public interface ContactDescriptor extends OpenApiDescriptor {

    String getName();
    void setName(String name);

    String getUrl();
    void setUrl(String url);

    String getEmail();
    void setEmail(String email);
}
