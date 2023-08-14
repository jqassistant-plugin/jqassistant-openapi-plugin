package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;

@Label("SecuritySchema")
public interface SecuritySchemeDescriptor extends OpenApiDescriptor{
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String name);
    @Relation("HAS_TYPE")
    String getType();
    void setType(String name);
    String getIn();
    void setIn(String in);
    String getScheme();
    void setScheme(String scheme);
    String getBearerFormat();
    void setBearerFormat(String bearerFormat);
    OauthFlowsDescriptor getFlows();
    void setFlows(OauthFlowsDescriptor flows);
    String getOpenIdConnectUrl();
    void setOpenIdConnectUrl(String openIdConnectUrl);

}
