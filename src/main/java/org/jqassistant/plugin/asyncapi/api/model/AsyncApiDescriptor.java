package org.jqassistant.plugin.asyncapi.api.model;

import com.buschmais.jqassistant.core.store.api.model.Descriptor;
import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.neo4j.api.annotation.Label;


@Label("AsyncAPI")
@Abstract
public interface AsyncApiDescriptor extends Descriptor {
}
