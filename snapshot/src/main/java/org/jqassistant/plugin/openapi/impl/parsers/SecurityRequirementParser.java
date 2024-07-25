package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.jqassistant.plugin.openapi.api.model.SecurityRequirementDescriptor;

import java.util.List;
import java.util.stream.Collectors;

public class SecurityRequirementParser {

    private SecurityRequirementParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<SecurityRequirementDescriptor> parseAll(List<SecurityRequirement> securityRequirements, Store store){
        return securityRequirements.stream().map(securityRequirement -> parseOne(securityRequirement, store)).collect(Collectors.toList());
    }

    public static SecurityRequirementDescriptor parseOne(SecurityRequirement securityRequirement, Store store){
        return store.create(SecurityRequirementDescriptor.class);
    }
}
