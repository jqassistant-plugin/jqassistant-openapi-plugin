package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.tags.Tag;
import org.jqassistant.plugin.openapi.api.model.TagDescriptor;

import java.util.List;
import java.util.stream.Collectors;

public class TagParser {

    private TagParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    // TODO: figure out how to name both parseAll functions equally

    public static List<TagDescriptor> parseAllTags(List<Tag> tags, Store store){
        return tags.stream().map(tag -> parseOne(tag, store)).collect(Collectors.toList());
    }

    public static List<TagDescriptor> parseAllStrings(List<String> tags, Store store) {
        return tags.stream().map(tag -> parseOne(tag, store)).collect(Collectors.toList());
    }

    public static TagDescriptor parseOne(Tag tag, Store store){
        return createTagDescriptor(tag.getName(), tag.getDescription(), tag.getExternalDocs(), store);
    }

    public static TagDescriptor parseOne(String tagName, Store store){
        return createTagDescriptor(tagName, null, null, store);
    }

    private static TagDescriptor createTagDescriptor(String tagName, String tagDescription, ExternalDocumentation externalDocs, Store store){
        TagDescriptor tagDescriptor = store.create(TagDescriptor.class);

        if(tagName != null && !tagName.isEmpty())
            tagDescriptor.setTag(tagName);
        if(tagDescription != null && !tagDescription.isEmpty())
            tagDescriptor.setDescription(tagDescription);
        if(externalDocs != null)
            tagDescriptor.setExternalDocs(ExternalDocsParser.parseOne(externalDocs, store));

        return tagDescriptor;
    }
}
