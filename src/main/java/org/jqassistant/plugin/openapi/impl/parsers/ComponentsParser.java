package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jqassistant.plugin.openapi.api.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentsParser {

    private ComponentsParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static ComponentsDescriptor parse(Components components, Store store){
        ComponentsDescriptor componentsDescriptor = store.create(ComponentsDescriptor.class);

        if (components.getSchemas() != null)
            componentsDescriptor.getSchemas().addAll(SchemaParser.parseAll(components.getSchemas(), store));

        if (components.getResponses() != null)
            componentsDescriptor.getResponses().addAll(ResponseParser.parseAll(components.getResponses(), store));

        if (components.getParameters() != null)
            componentsDescriptor.getParameters().addAll(ParameterParser.parseAll(components.getParameters(), store));

        if (components.getExamples() != null)
            componentsDescriptor.getExamples().addAll(ExampleParser.parseAll(components.getExamples(), store));

        if (components.getRequestBodies() != null)
            componentsDescriptor.getRequestBodies().addAll(RequestBodyParser.parseAll(components.getRequestBodies(), store));

        if (components.getHeaders() != null)
            componentsDescriptor.getHeaders().addAll(HeaderParser.parseAll(components.getHeaders(), store));

        if (components.getSecuritySchemes() != null)
            componentsDescriptor.getSecuritySchemas().addAll(parseSecuritySchemes(components.getSecuritySchemes(), store));

        if (components.getLinks() != null)
            componentsDescriptor.getLinks().addAll(LinkParser.parseAll(components.getLinks(), store));

        if (components.getCallbacks() != null)
            componentsDescriptor.getCallBacks().addAll(CallbackParser.parseAll(components.getCallbacks(), store));

        return componentsDescriptor;
    }

    private static List<SecuritySchemeDescriptor> parseSecuritySchemes(Map<String, SecurityScheme> securitySchemesMap, Store store){
        List<SecuritySchemeDescriptor> securitySchemeDescriptors = new ArrayList<>();
        securitySchemesMap.forEach((s, securityScheme) -> securitySchemeDescriptors.add(parseSecurityScheme(securityScheme, store)));
        return securitySchemeDescriptors;
    }

    private static SecuritySchemeDescriptor parseSecurityScheme(SecurityScheme securityScheme, Store store){
        SecuritySchemeDescriptor securitySchemeDescriptor = store.create(SecuritySchemeDescriptor.class);

        if(securityScheme.getName() != null)
            securitySchemeDescriptor.setName(securityScheme.getName());

        return securitySchemeDescriptor;
    }
}
