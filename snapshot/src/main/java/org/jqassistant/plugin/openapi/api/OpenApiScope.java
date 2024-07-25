package org.jqassistant.plugin.openapi.api;

import com.buschmais.jqassistant.core.scanner.api.Scope;

public enum OpenApiScope implements Scope {

    CONTRACT;

    @Override
    public String getPrefix() {
        return "openapi";
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
