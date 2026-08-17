package org.jqassistant.plugin.openapi.impl.parsers;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.jqassistant.plugin.openapi.api.model.ContactDescriptor;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.jqassistant.plugin.openapi.api.model.ExtensionDescriptor;
import org.jqassistant.plugin.openapi.api.model.InfoDescriptor;
import org.jqassistant.plugin.openapi.api.model.LicenseDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContractParser {

    private static final Logger LOG = LoggerFactory.getLogger(ContractParser.class);

    private ContractParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void parse(OpenAPI contract, ContractDescriptor contractDescriptor, Store store){
        LOG.info("Reading OpenAPI Version");
        contractDescriptor.setOpenApiVersion(contract.getOpenapi());
        LOG.info("Reading Info object");
        if(contract.getInfo() != null)
            contractDescriptor.setInfo(parseInfo(contract.getInfo(), store));
        LOG.info("Reading jsonSchema dialect");
        contractDescriptor.setJsonSchemaDialect(contract.getJsonSchemaDialect());
        LOG.info("Parsing Servers");
        if (contract.getServers() != null)
            contractDescriptor.getServers().addAll(ServerParser.parseAll(contract.getServers(), store));
        LOG.info("Parsing Paths");
        if (contract.getPaths() != null)
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

    private static InfoDescriptor parseInfo(Info info, Store store){
        InfoDescriptor infoDescriptor = store.create(InfoDescriptor.class);

        // null check only needed for non-string properties as null strings are filtered out by xo

        infoDescriptor.setTitle(info.getTitle());
        infoDescriptor.setSummary(info.getSummary());
        infoDescriptor.setDescription(info.getDescription());
        infoDescriptor.setTermsOfService(info.getTermsOfService());

        if(info.getContact() != null)
            infoDescriptor.setContact(parseContact(info.getContact(), store));
        if(info.getLicense() != null)
            infoDescriptor.setLicense(parseLicense(info.getLicense(), store));
        if(info.getExtensions() != null)
            infoDescriptor.getExtensions().addAll(parseExtensions(info.getExtensions(), store));

        infoDescriptor.setVersion(info.getVersion());

        return infoDescriptor;
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

    private static LicenseDescriptor parseLicense(License license, Store store){
        LicenseDescriptor licenseDescriptor = store.create(LicenseDescriptor.class);

        licenseDescriptor.setName(license.getName());
        licenseDescriptor.setIdentifier(license.getIdentifier());
        licenseDescriptor.setUrl(license.getUrl());

        return licenseDescriptor;
    }

    private static List<ExtensionDescriptor> parseExtensions(Map<String, Object> extensions, Store store){
        return extensions.keySet().stream()
                .map(key -> parseExtension(key, extensions.get(key), store))
                .collect(Collectors.toList());
    }

    private static ExtensionDescriptor parseExtension(String key, Object value, Store store){
        ExtensionDescriptor extensionDescriptor = store.create(ExtensionDescriptor.class);

        extensionDescriptor.setKey(key);
        extensionDescriptor.setValue(value);

        return extensionDescriptor;
    }
}
