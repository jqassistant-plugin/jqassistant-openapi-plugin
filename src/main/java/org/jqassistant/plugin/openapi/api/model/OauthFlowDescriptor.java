package org.jqassistant.plugin.openapi.api.model;

import java.util.List;

public interface OauthFlowDescriptor extends OpenApiDescriptor{

    String getAuthorizationUrl();

    void setAuthorizationUrl(String authorizationUrl);

    String getTokenUrl();

    void setTokenUrl(String tokenUrl);

    String getRefreshUrl();

    void setRefreshUrl(String refreshUrl);

    List<String> getScopes();

    void setScopes(List<String> scopes);
}
