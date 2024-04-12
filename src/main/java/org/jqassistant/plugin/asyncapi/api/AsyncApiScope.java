package org.jqassistant.plugin.asyncapi.api;

import com.buschmais.jqassistant.core.scanner.api.Scope;

public enum AsyncApiScope implements Scope {

    CONTRACT;

    @Override
    public String getPrefix() {
        return "asyncapi";
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
