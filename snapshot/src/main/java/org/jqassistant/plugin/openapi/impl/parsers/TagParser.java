package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.jqassistant.plugin.openapi.api.model.TagDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TagParser {

    private TagParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static List<TagDescriptor> parseAll(List<?> tags, Store store) {
        if(!tags.isEmpty() && tags.get(0) instanceof String)
            return tags.stream().map(String.class::cast).map(tagName -> parseOne(tagName, null, null, store)).collect(Collectors.toList());
        else if(!tags.isEmpty() && tags.get(0) instanceof Tag)
            return tags.stream().map(Tag.class::cast).map(tag -> parseOne(tag.getName(), tag.getDescription(), tag.getExternalDocs(), store)).collect(Collectors.toList());
        else
            return new ArrayList<>();
    }

    private static TagDescriptor parseOne(String tagName, String tagDescription, ExternalDocumentation externalDocs, Store store){
        TagDescriptor tagDescriptor = store.create(TagDescriptor.class);

        tagDescriptor.setName(tagName);

        if(tagDescription != null)
            tagDescriptor.setDescription(tagDescription);
        if(externalDocs != null)
            tagDescriptor.setExternalDocs(ExternalDocsParser.parseOne(externalDocs, store));

        return tagDescriptor;
    }
}
