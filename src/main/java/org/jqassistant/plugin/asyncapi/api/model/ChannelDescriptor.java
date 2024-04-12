package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Channel")
public interface ChannelDescriptor extends AsyncApiDescriptor {
    String getAddress();
    void setAddress(String address);

    String getTitle();
    void setTitle(String title);

    String getSummary();
    void setSummary(String summary);

    String getDescription();
    void setDescription(String description);
}

