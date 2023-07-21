package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.tags.Tag;
import org.jqassistant.plugin.openapi.api.model.TagDescriptor;

import java.util.ArrayList;
import java.util.List;

public class TagParser {

    private TagParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    // TODO: figure out how to name both parseAll functions equally

    public static List<TagDescriptor> parseAllTags(List<Tag> tags, Store store){
        List<TagDescriptor> tagDescriptors = new ArrayList<>();
        tags.forEach(tag -> tagDescriptors.add(parseOne(tag, store)));
        return tagDescriptors;
    }

    public static List<TagDescriptor> parseAllStrings(List<String> tags, Store store) {
        List<TagDescriptor> tagDescriptors = new ArrayList<>();
        tags.forEach(tag -> tagDescriptors.add(parseOne(tag, store)));
        return tagDescriptors;
    }

    public static TagDescriptor parseOne(Tag tag, Store store){
        TagDescriptor tagDescriptor = store.create(TagDescriptor.class);

        if(tag.getName() != null && !tag.getName().isEmpty())
            tagDescriptor.setTag(tag.getName());
        if(tag.getDescription() != null && !tag.getDescription().isEmpty())
            tagDescriptor.setDescription(tag.getDescription());

        return tagDescriptor;
    }

    public static TagDescriptor parseOne(String tagName, Store store){
        TagDescriptor tagDescriptor = store.create(TagDescriptor.class);

        if(tagName != null && !tagName.isEmpty())
            tagDescriptor.setTag(tagName);

        return tagDescriptor;
    }
}
