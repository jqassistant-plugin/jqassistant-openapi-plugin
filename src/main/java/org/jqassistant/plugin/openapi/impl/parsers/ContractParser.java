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

    public static void parse(OpenAPI contract, ContractDescriptor contractDescriptor, Store store) {

        LOG.info("Parsing Contract Metadata");
        contractDescriptor.setOpenApiVersion(contract.getOpenapi()); // required
        /*
        contract.setInfo(parseInfo, store); // info object required
        */
        if (contract.getJsonSchemaDialect() != null)
            contractDescriptor.setJsonSchemaDialect(contract.getJsonSchemaDialect());
        LOG.info("Parsing Servers");
        if (contract.getServers() != null && !contract.getServers().isEmpty())
            contractDescriptor.getServers().addAll(ServerParser.parseAll(contract.getServers(), store));
        LOG.info("Parsing Paths");
        if (contract.getPaths() != null && !contract.getPaths().isEmpty())
            contractDescriptor.setPaths(PathsParser.parse(contract.getPaths(), store));
        LOG.info("Parsing Webhooks");
        if (contract.getWebhooks() != null)
            contractDescriptor.getWebhooks().addAll(PathsParser.parsePathItems(contract.getWebhooks(), store));
        LOG.info("Parsing Components");
        if (contract.getComponents() != null)
            contractDescriptor.setComponents(ComponentsParser.parse(contract.getComponents(), store));
        LOG.info("Parsing Security");
        if (contract.getSecurity() != null)
            contractDescriptor.getSecurity().addAll(SecurityRequirementParser.parseAll(contract.getSecurity(), store));
        LOG.info("Parsing Tags");
        if (contract.getTags() != null)
            contractDescriptor.getTags().addAll(TagParser.parseAll(contract.getTags(), store));
        LOG.info("Parsing External Documentation");
        if (contract.getExternalDocs() != null)
            contractDescriptor.setExternalDocs(ExternalDocsParser.parseOne(contract.getExternalDocs(), store));
    }

    /*
    private static InfoDescriptor parseInfo(Info info, Store store){
       //TODO implement
    }
    */
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
