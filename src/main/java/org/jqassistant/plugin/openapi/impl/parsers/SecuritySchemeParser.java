package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.OauthFlowDescriptor;
import org.jqassistant.plugin.openapi.api.model.OauthFlowsDescriptor;
import org.jqassistant.plugin.openapi.api.model.SecuritySchemeDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecuritySchemeParser {

    private SecuritySchemeParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<SecuritySchemeDescriptor> parseAll(Map<String, SecurityScheme> securitySchemesMap, Store store){
        return securitySchemesMap.values().stream().map(securityScheme -> parseOne(securityScheme, store)).collect(Collectors.toList());
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
         securitySchemeDescriptor.setFlows(parseOauthFlows(securityScheme.getFlows(), store));

        if(securityScheme.getOpenIdConnectUrl() != null)
            securitySchemeDescriptor.setOpenIdConnectUrl(securityScheme.getOpenIdConnectUrl());

        return securitySchemeDescriptor;
    }



    private static OauthFlowsDescriptor parseOauthFlows(OAuthFlows oAuthFlows, Store store) {
        OauthFlowsDescriptor oauthFlowsDescriptor = store.create(OauthFlowsDescriptor.class);

        if (oAuthFlows.getImplicit() != null) {
            OauthFlowDescriptor implicit = parseOauthFlow(oAuthFlows.getImplicit(), store);
            oauthFlowsDescriptor.setImplicit(implicit);
        }
        if (oAuthFlows.getPassword() != null) {
            OauthFlowDescriptor password = parseOauthFlow(oAuthFlows.getPassword(), store);
            oauthFlowsDescriptor.setPassword(password);
        }
        if (oAuthFlows.getClientCredentials() != null) {
            OauthFlowDescriptor clientCredentials = parseOauthFlow(oAuthFlows.getClientCredentials(), store);
            oauthFlowsDescriptor.setClientCredentials(clientCredentials);
        }
        if (oAuthFlows.getAuthorizationCode() != null) {
            OauthFlowDescriptor authorizationCode = parseOauthFlow(oAuthFlows.getAuthorizationCode(), store);
            oauthFlowsDescriptor.setAuthorizationCode(authorizationCode);
        }

        return oauthFlowsDescriptor;
    }

    private static OauthFlowDescriptor parseOauthFlow(OAuthFlow oauthFlow, Store store) {
        OauthFlowDescriptor oauthFlowDescriptor = store.create(OauthFlowDescriptor.class);

        if(oauthFlow.getAuthorizationUrl() != null)
            oauthFlowDescriptor.setAuthorizationUrl(oauthFlow.getAuthorizationUrl());
        if(oauthFlow.getTokenUrl() != null)
            oauthFlowDescriptor.setTokenUrl(oauthFlow.getTokenUrl());
        if(oauthFlow.getRefreshUrl() != null)
            oauthFlowDescriptor.setRefreshUrl(oauthFlow.getRefreshUrl());
        if(oauthFlow.getScopes() != null)
            oauthFlowDescriptor.setScopes(oauthFlow.getScopes());


        return oauthFlowDescriptor;
    }



}
