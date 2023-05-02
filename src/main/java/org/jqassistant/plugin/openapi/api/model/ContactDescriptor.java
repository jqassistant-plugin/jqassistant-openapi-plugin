package org.jqassistant.plugin.openapi.api.model;

public interface ContactDescriptor extends OpenApiDescriptor {

    String getName();
    void setName(String name);

    String getUrl();
    void setUrl(String url);

    String getEmail();
    void setEmail(String email);
}
