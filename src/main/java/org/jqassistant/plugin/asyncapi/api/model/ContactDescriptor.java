package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Contact")
public interface ContactDescriptor extends AsyncApiDescriptor {
    String getName();
    void setName(String name);

    String getUrl();
    void setUrl(String url);

    String getEmail();
    void setEmail(String email);
}
