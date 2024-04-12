package org.jqassistant.plugin.asyncapi.impl;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import impl.generated.AsyncApi;
import impl.generated.Contact;
import impl.generated.Info;
import impl.generated.License;
import org.jqassistant.plugin.asyncapi.api.AsyncApiScope;
import org.jqassistant.plugin.asyncapi.api.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Requires(FileDescriptor.class)
public class AsyncAPIScannerPlugin extends AbstractScannerPlugin<FileResource, ContractDescriptor> {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncAPIScannerPlugin.class);

    @Override
    public boolean accepts(FileResource fileResource, String path, Scope scope) {
        return AsyncApiScope.CONTRACT.equals(scope) &&
                (path.toLowerCase().endsWith(".yml") || path.toLowerCase().endsWith(".yaml"));
    }

    @Override
    public ContractDescriptor scan(FileResource fileResource, String path, Scope scope, Scanner scanner) throws IOException {
        LOG.info("Starting scanning process for {}", path);
        final Store store = scanner.getContext().getStore();
        final FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
        final ContractDescriptor contractDescriptor = store.addDescriptorType(fileDescriptor, ContractDescriptor.class);

        try (InputStream inputStream = fileResource.createStream()) {
            AsyncApi asyncApi = parseYaml(inputStream);
            map(asyncApi, contractDescriptor, store);
        }

        return contractDescriptor;
    }

    private static void map(AsyncApi asyncApi, ContractDescriptor contractDescriptor, Store store) {
        String asyncapi = asyncApi.getAsyncapi();
        contractDescriptor.setAsyncApiVersion(asyncapi);
        InfoDescriptor infoDescriptor = map(asyncApi.getInfo(), store);
        //  List<Channels> channels = mapChannels(asyncApi.getChannels(), store);
        contractDescriptor.setInfo(infoDescriptor);
    }

    private static InfoDescriptor map(Info info, Store store) {
        InfoDescriptor infoDescriptor = store.create(InfoDescriptor.class);
        infoDescriptor.setDescription(info.getDescription());
        infoDescriptor.setTermsOfService(info.getTermsOfService().toString());
        infoDescriptor.setContact(map(info.getContact(), store));
        infoDescriptor.setLicense(map(info.getLicense(), store));
        infoDescriptor.getTags().addAll(map(info.getTags(), store));
        infoDescriptor.setExternalDocumentation(map(info.getExternalDocs(), store));
        return infoDescriptor;
    }

    /**
     * private static List<Channels>  mapChannels(Channels channels, Store store){
     * ChannelDescriptor channelDescriptor = store.create(ChannelDescriptor.class);
     * channelDescriptor.setTitle(channels.toString());
     * //channels.getAdditionalProperties().get()
     * return null;
     * <p>
     * }
     **/


    private static ContactDescriptor map(Contact contact, Store store) {
        ContactDescriptor contactDescriptor = store.create(ContactDescriptor.class);
        contactDescriptor.setName(contact.getName());
        contactDescriptor.setUrl(contact.getUrl().toString());
        contactDescriptor.setEmail(contact.getEmail());
        return contactDescriptor;
    }


    private static List<TagDescriptor> map(Set<Object> tags, Store store) {
        return tags.stream().map(tag -> {
            TagDescriptor tagDescriptor = store.create(TagDescriptor.class);
            if (tag instanceof Map) {
                Map<String, Object> tagMap = (Map<String, Object>) tag;
                String ref = (String) tagMap.get("$ref");
                if (ref != null) {
                    tagDescriptor.setRef(ref);
                } else {
                    tagDescriptor.setName((String) tagMap.get("name"));
                    tagDescriptor.setDescription((String) tagMap.get("description"));
                }
            }
            return tagDescriptor;
        }).collect(toList());
    }

    private static LicenseDescriptor map(License license, Store store) {
        LicenseDescriptor licenseDescriptor = store.create(LicenseDescriptor.class);
        licenseDescriptor.setName(license.getName());
        licenseDescriptor.setUrl(license.getUrl().toString());
        return licenseDescriptor;
    }


    private static ExternalDocsDescriptor map(Object externalDocs, Store store) {
        Map<String, String> docs = (Map<String, String>) externalDocs;
        ExternalDocsDescriptor externalDocsDescriptor = store.create(ExternalDocsDescriptor.class);
        if (docs.get("$ref") != null) {
            externalDocsDescriptor.setUrl(docs.get("$ref"));
        } else {
            externalDocsDescriptor.setDescription(docs.get("description"));
            externalDocsDescriptor.setUrl(docs.get("url"));

        }
        return externalDocsDescriptor;
    }


    private AsyncApi parseYaml(InputStream inputStream) throws IOException {
        ObjectMapper mapper = new YAMLMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(inputStream, AsyncApi.class);
    }
}
