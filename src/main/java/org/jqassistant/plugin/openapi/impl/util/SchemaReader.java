package org.jqassistant.plugin.openapi.impl.util;


import com.buschmais.jqassistant.core.store.api.Store;
import io.swagger.v3.oas.models.media.Schema;
import org.jqassistant.plugin.openapi.api.model.jsonschema.ObjectPropertyDescriptor;
import org.jqassistant.plugin.openapi.api.model.jsonschema.SchemaDescriptor;
import org.jqassistant.plugin.openapi.impl.jsonschema.JSONSchemaObjectReader;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SchemaReader implements Runnable {

    private final LinkedBlockingQueue<SchemaDescriptor> list;
    private final String name;
    private final Schema<?> schema;

    private final Store store;

    private final Lock lock;

    private final Condition newElementPresent;

    public SchemaReader(LinkedBlockingQueue<SchemaDescriptor> list, String name, Schema<?> schema, Store store, Lock lock, Condition newElementPresent) {
        this.list = list;
        this.name = name;
        this.schema = schema;
        this.store = store;
        this.lock = lock;
        this.newElementPresent = newElementPresent;
    }

    @Override
    public void run() {
        SchemaDescriptor schemaDescriptor = store.create(SchemaDescriptor.class);

        if (Objects.equals(schema.getType(), ObjectPropertyDescriptor.TYPE_NAME)){

            lock.lock();
            try {
                schemaDescriptor.setObject(new JSONSchemaObjectReader(store, newElementPresent).parseObject(name, schema));
                newElementPresent.signalAll();
            } finally {
                lock.unlock();
            }
        } else {
            throw new RuntimeException("Unknown schema!");
        }

        schemaDescriptor.setName(name);

        list.add(schemaDescriptor);
    }
}
