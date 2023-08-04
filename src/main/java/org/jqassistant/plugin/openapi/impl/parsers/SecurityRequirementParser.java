package org.jqassistant.plugin.openapi.impl.parsers;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.jqassistant.plugin.openapi.api.model.SecurityRequirementDescriptor;

import java.util.ArrayList;
import java.util.List;

public class SecurityRequirementParser {

    private SecurityRequirementParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<SecurityRequirementDescriptor> parseAll(List<SecurityRequirement> securityRequirements){
        return new ArrayList<>();
    }
}
