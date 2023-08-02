package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.jqassistant.plugin.openapi.api.model.ParameterDescriptor;
import org.jqassistant.plugin.openapi.impl.jsonschema.JsonSchemaParser;
import org.jqassistant.plugin.openapi.impl.util.Resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterParser {

    private ParameterParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<ParameterDescriptor> parseAll(Map<String, Parameter> parametersMap, Store store){
        return parametersMap.values().stream().map(parameter -> parseOne(parameter, store)).collect(Collectors.toList());
    }

    public static List<ParameterDescriptor> parseAll(List<Parameter> parameters, Store store){
        return parameters.stream().map(parameter -> parseOne(parameter, store)).collect(Collectors.toList());
    }

    public static ParameterDescriptor parseOne(Parameter parameter, Store store){
        ParameterDescriptor parameterDescriptor = store.create(ParameterDescriptor.class);
        JsonSchemaParser schemaParser = new JsonSchemaParser(new Resolver(store), store);

        parameterDescriptor.setName(parameter.getName());

        if(parameter.getIn() != null)
            parameterDescriptor.setLocation(ParameterDescriptor.ParameterLocation.valueOf(parameter.getIn().toUpperCase()));
        if(parameter.getDescription() != null)
            parameterDescriptor.setDescription(parameter.getDescription());
        if(parameter.getRequired() != null)
            parameterDescriptor.setIsRequired(parameter.getRequired());
        if(parameter.getDeprecated() != null)
            parameterDescriptor.setIsDeprecated(parameter.getDeprecated());
        if(parameter.getAllowEmptyValue() != null)
            parameterDescriptor.setAllowsEmptyValue(parameter.getAllowEmptyValue());
        if(parameter.getStyle() != null)
            parameterDescriptor.setStyle(parameter.getStyle());
        if(parameter.getExplode() != null)
            parameterDescriptor.setExplode(parameter.getExplode());
        if(parameter.getAllowReserved() != null)
            parameterDescriptor.setAllowsReserved(parameter.getAllowReserved());
        if(parameter.getSchema() != null)
            parameterDescriptor.setSchema(schemaParser.parseSchema(parameter.getSchema(), parameter.getSchema().getName()));
        if(parameter.getExample() != null)
            parameterDescriptor.setExample(parameter.getExample());
        if(parameter.getExamples() != null)
            parameterDescriptor.getExamples().addAll(ExampleParser.parseAll(parameter.getExamples(), store));
        if(parameter.getContent() != null)
            parameterDescriptor.getContent().addAll(MediaTypeParser.parseAll(parameter.getContent(), store));

        return parameterDescriptor;
    }
}
