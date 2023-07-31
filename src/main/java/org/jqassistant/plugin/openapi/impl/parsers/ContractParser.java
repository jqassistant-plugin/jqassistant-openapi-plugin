package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.jqassistant.plugin.openapi.api.model.ContactDescriptor;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractParser {

    private static final Logger LOG = LoggerFactory.getLogger(ContractParser.class);

    private ContractParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void parse(OpenAPI contract, ContractDescriptor contractDescriptor, Store store){
        LOG.info("Reading Info object");
        if(contract.getInfo() != null)
            parseInfo(contract.getInfo(), contractDescriptor, store);

        LOG.info("Reading OpenAPI Components");
        if (contract.getComponents() != null)
            contractDescriptor.setComponents(ComponentsParser.parse(contract.getComponents(), store));

        LOG.info("Reading OpenAPI Tags");
        if(contract.getTags() != null && !contract.getTags().isEmpty())
            contractDescriptor.getTags().addAll(TagParser.parseAllTags(contract.getTags(), store));

        LOG.info("Reading OpenAPI Servers");
        if(contract.getServers() != null && !contract.getServers().isEmpty())
            contractDescriptor.getServers().addAll(ServerParser.parseAll(contract.getServers(), store));

        LOG.info("Reading OpenAPI Paths");
        if(contract.getPaths() != null && !contract.getPaths().isEmpty())
            contractDescriptor.getPaths().addAll(PathParser.parseAll(contract.getPaths(), store));

        if(contract.getExternalDocs() != null)
            contractDescriptor.setExternalDocs(ExternalDocsParser.parseOne(contract.getExternalDocs(), store));
}

    private static void parseInfo(Info info, ContractDescriptor contractDescriptor, Store store){
        if(info.getTitle() != null)
            contractDescriptor.setTitle(info.getTitle());
        if (info.getDescription() != null)
            contractDescriptor.setDescription(info.getDescription());
        if (info.getVersion() != null)
            contractDescriptor.setApiVersion(info.getVersion());
        if (info.getContact() != null)
            contractDescriptor.setContact(parseContact(info.getContact(), store));
    }

    private static ContactDescriptor parseContact(Contact contact, Store store){
        ContactDescriptor contactDescriptor = store.create(ContactDescriptor.class);

        if(contact.getName() != null)
            contactDescriptor.setName(contact.getName());
        if(contact.getEmail() != null)
            contactDescriptor.setEmail(contact.getEmail());
        if(contact.getUrl() != null)
            contactDescriptor.setUrl(contact.getUrl());

        return contactDescriptor;
    }

}
