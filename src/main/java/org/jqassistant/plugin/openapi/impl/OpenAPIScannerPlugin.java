package org.jqassistant.plugin.openapi.impl;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.jqassistant.plugin.openapi.api.model.*;
import org.jqassistant.plugin.openapi.impl.parsers.ContractParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Requires(FileDescriptor.class)
public class OpenAPIScannerPlugin extends AbstractScannerPlugin<FileResource, ContractDescriptor> {

    private static final Logger LOG = LoggerFactory.getLogger(OpenAPIScannerPlugin.class);

    @Override
    public boolean accepts(FileResource fileResource, String path, Scope scope) throws IOException {
        return path.toLowerCase().endsWith(".yaml");  // TODO maybe add more testing
    }

    @Override
    public ContractDescriptor scan(FileResource fileResource, String path, Scope scope, Scanner scanner) throws IOException {
        LOG.info("Starting scanning process");

        ScannerContext context = scanner.getContext();
        final Store store = scanner.getContext().getStore();
        final FileDescriptor fileDescriptor = context.getCurrentDescriptor();
        final ContractDescriptor contractDescriptor = store.addDescriptorType(fileDescriptor, ContractDescriptor.class);

        LOG.info("Reading OpenAPI document from path: {}", path);
        OpenAPI openAPIContract = new OpenAPIV3Parser().read(path); // TODO: Exception handling

        LOG.info("Reading contract meta data");
        contractDescriptor.setApiVersion(openAPIContract.getOpenapi());

        LOG.info("Parsing contract children");
        ContractParser.parse(openAPIContract, contractDescriptor,store);

        LOG.info("...finished");
        return contractDescriptor;
    }
}
