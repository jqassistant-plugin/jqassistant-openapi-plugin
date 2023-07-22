package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.jqassistant.plugin.openapi.api.model.ParameterDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParameterParser {

    private ParameterParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<ParameterDescriptor> parseAll(Map<String, Parameter> parametersMap, Store store){
        List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
        parametersMap.forEach((s, parameter) -> parameterDescriptors.add(parseOne(parameter, store)));
        return parameterDescriptors;
    }

    public static List<ParameterDescriptor> parseAll(List<Parameter> parameters, Store store){
        List<ParameterDescriptor> parameterDescriptors = new ArrayList<>();
        parameters.forEach(parameter -> parameterDescriptors.add(parseOne(parameter, store)));
        return parameterDescriptors;
    }
    public static ParameterDescriptor parseOne(Parameter parameter, Store store){
        ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);

        if(parameter.getName() != null && !parameter.getName().isEmpty())
            parameterDescriptor.setName(parameter.getName());
        if(parameter.getIn() != null && !parameter.getIn().isEmpty())
            parameterDescriptor.setLocation(ParameterDescriptor.ParameterLocation.valueOf(parameter.getIn().toUpperCase()));
        if(parameter.getDescription() != null && !parameter.getDescription().isEmpty())
            parameterDescriptor.setDescription(parameter.getDescription());
        if(parameter.getRequired() != null)
            parameterDescriptor.setIsRequired(parameter.getRequired());
        if(parameter.getDeprecated() != null)
            parameterDescriptor.setIsDeprecated(parameter.getDeprecated());
        if(parameter.getAllowEmptyValue() != null)
            parameterDescriptor.setAllowsEmptyValue(parameter.getAllowEmptyValue());

        return parameterDescriptor;
    }

}
