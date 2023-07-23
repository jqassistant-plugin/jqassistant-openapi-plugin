package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.examples.Example;
import org.jqassistant.plugin.openapi.api.model.ExampleDescriptor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExampleParser {

    private ExampleParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<ExampleDescriptor> parseAll(Map<String, Example> examplesMap, Store store){
        return examplesMap.values().stream().map(example -> parseOne(example, store)).collect(Collectors.toList());
    }

    public static ExampleDescriptor parseOne(Example example, Store store){
        ExampleDescriptor exampleDescriptor = store.create(ExampleDescriptor.class);

        if(exampleDescriptor.getDescription() != null)
            exampleDescriptor.setDescription(example.getDescription());

        return exampleDescriptor;
    }
}
