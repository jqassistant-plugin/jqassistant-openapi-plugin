package org.jqassistant.plugin.openapi.api.model;

public interface OauthFlowsDescriptor extends OpenApiDescriptor{

    OauthFlowDescriptor getImplicit();
    void setImplicit(OauthFlowDescriptor implicit);
    OauthFlowDescriptor getPassword();
    void setPassword(OauthFlowDescriptor password);
    OauthFlowDescriptor getClientCredentials();
    void setClientCredentials(OauthFlowDescriptor clientCredentials);
    OauthFlowDescriptor getAuthorizationCode();
    void setAuthorizationCode(OauthFlowDescriptor authorizationCode);
}
