package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.jqassistant.plugin.openapi.api.model.ContractDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContractParser {

    private static final Logger LOG = LoggerFactory.getLogger(ContractParser.class);

    private ContractParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static void parse(OpenAPI contract, ContractDescriptor contractDescriptor, Store store){
        // TODO extend contract parsing to full coverage

        LOG.info("Reading Info object");
        if(contract.getInfo() != null)
            parseInfo(contract.getInfo(), contractDescriptor, store);

        LOG.info("Reading OpenAPI Components");
        if (contract.getComponents() != null)
            contractDescriptor.setComponents(ComponentsParser.parse(contract.getComponents(), store));

        LOG.info("Reading OpenAPI Tags");
        if(contract.getTags() != null && !contract.getTags().isEmpty())
            contractDescriptor.getTags().addAll(Parsers.parseTags(contract.getTags(), store));

        LOG.info("Reading OpenAPI Servers");
        if(contract.getServers() != null && !contract.getServers().isEmpty())
            contractDescriptor.getServers().addAll(Parsers.parseSevers(contract.getServers(), store));

        LOG.info("Reading OpenAPI Paths");
        if(contract.getPaths() != null && !contract.getPaths().isEmpty())
            contractDescriptor.getPaths().addAll(Parsers.parsePaths(contract.getPaths(), store));
    }

    /**
     * Parses OpenAPI info object to internal properties
     * @param info object to parse
     * @param contractDescriptor object on which properties get set
     * @param store store object to create internal object from
     */
    private static void parseInfo(Info info, ContractDescriptor contractDescriptor, Store store){
        if(info.getTitle() != null)
            contractDescriptor.setTitle(info.getTitle());
        if (info.getDescription() != null)
            contractDescriptor.setDescription(info.getDescription());
        if (info.getVersion() != null)
            contractDescriptor.setApiVersion(info.getVersion());
        if (info.getContact() != null)
            contractDescriptor.setContact(Parsers.parseContact(info.getContact(), store));
    }
}
