package org.jqassistant.plugin.openapi.api.model;

import io.swagger.v3.oas.models.security.Scopes;

import java.util.List;

public interface OauthFlowDescriptor extends OpenApiDescriptor{

    String getAuthorizationUrl();

    void setAuthorizationUrl(String authorizationUrl);

    String getTokenUrl();

    void setTokenUrl(String tokenUrl);

    String getRefreshUrl();

    void setRefreshUrl(String refreshUrl);

    List<String> getScopes();

    void setScopes(Scopes scopes);
}
