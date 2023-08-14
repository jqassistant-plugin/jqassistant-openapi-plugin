package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.OauthFlowDescriptor;
import org.jqassistant.plugin.openapi.api.model.SecuritySchemeDescriptor;

public class SecuritySchemeParser {

    private SecuritySchemeParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static SecuritySchemeDescriptor parseOne(SecurityScheme securityScheme, Store store){
        SecuritySchemeDescriptor securitySchemeDescriptor = store.create(SecuritySchemeDescriptor.class);

        if(securityScheme.getName() != null)
            securitySchemeDescriptor.setName(securityScheme.getName());

        if(securityScheme.getType() != null)
            securitySchemeDescriptor.setType(securityScheme.getType().toString());

        if(securityScheme.getIn() != null)
            securitySchemeDescriptor.setIn(securityScheme.getIn().toString());

        if(securityScheme.getDescription() != null)
            securitySchemeDescriptor.setDescription(securityScheme.getDescription());

        if(securityScheme.getScheme() != null)
            securitySchemeDescriptor.setScheme(securityScheme.getScheme());

        if(securityScheme.getBearerFormat() != null)
            securitySchemeDescriptor.setBearerFormat(securityScheme.getBearerFormat());

        if(securityScheme.getFlows() != null)
   //      securitySchemeDescriptor.setFlows(parseOauthFlows(securityScheme.getFlows(), store));

        if(securityScheme.getOpenIdConnectUrl() != null)
            securitySchemeDescriptor.setOpenIdConnectUrl(securityScheme.getOpenIdConnectUrl());

        return securitySchemeDescriptor;
    }



    public static void parseOauthFlows(OAuthFlows oauthFlowsDescriptor, OAuthFlows oAuthFlows) {
        if (oAuthFlows.getImplicit() != null) {
            OAuthFlow implicit = parseOauthFlow(oAuthFlows.getImplicit());
            oauthFlowsDescriptor.setImplicit(implicit);
        }
        if (oAuthFlows.getPassword() != null) {
            OAuthFlow password = parseOauthFlow(oAuthFlows.getPassword());
            oauthFlowsDescriptor.setPassword(password);
        }
        if (oAuthFlows.getClientCredentials() != null) {
            OAuthFlow clientCredentials = parseOauthFlow(oAuthFlows.getClientCredentials());
            oauthFlowsDescriptor.setClientCredentials(clientCredentials);
        }
        if (oAuthFlows.getAuthorizationCode() != null) {
            OAuthFlow authorizationCode = parseOauthFlow(oAuthFlows.getAuthorizationCode());
            oauthFlowsDescriptor.setAuthorizationCode(authorizationCode);
        }
    }

    public static OAuthFlow parseOauthFlow(OAuthFlow oauthFlow){
        if(oauthFlow.getAuthorizationUrl() != null)
            oauthFlow.setAuthorizationUrl(oauthFlow.getAuthorizationUrl());
        if(oauthFlow.getTokenUrl() != null)
            oauthFlow.setTokenUrl(oauthFlow.getTokenUrl());
        if(oauthFlow.getRefreshUrl() != null)
            oauthFlow.setRefreshUrl(oauthFlow.getRefreshUrl());
        if(oauthFlow.getScopes() != null)
            oauthFlow.setScopes(oauthFlow.getScopes());


        return oauthFlow;
    }



}
