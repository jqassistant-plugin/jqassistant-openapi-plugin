package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.jqassistant.plugin.openapi.api.model.ExternalDocsDescriptor;

public class ExternalDocsParser {
    private ExternalDocsParser() {throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");}

    public static ExternalDocsDescriptor parseOne(ExternalDocumentation externalDocs, Store store){
        ExternalDocsDescriptor externalDocsDescriptor = store.create(ExternalDocsDescriptor.class);

        if(externalDocs.getDescription() != null)
            externalDocsDescriptor.setDescription(externalDocs.getDescription());
        if(externalDocs.getUrl() != null)
            externalDocsDescriptor.setUrl(externalDocs.getUrl());

        return externalDocsDescriptor;
    }
}
