package org.jqassistant.plugin.openapi.api.model;

import com.buschmais.xo.neo4j.api.annotation.Label;
import com.buschmais.xo.neo4j.api.annotation.Relation;
import io.swagger.v3.oas.models.media.Encoding;

import java.util.List;

@Label("ENCODING")
public interface EncodingDescriptor extends OpenApiDescriptor{

    String getPropertyName();
    void setPropertyName(String propertyName);

    String getContentType();
    void setContentType(String contentType);

    @Relation("USES_HEADER")
    List<HeaderDescriptor> getHeaders();

    Encoding.StyleEnum getStyle();
    void setStyle(Encoding.StyleEnum style);

    boolean getExplode();
    void setExplode(boolean explode);

    boolean getAllowsReserved();
    void setAllowsReserved(boolean allowsReserved);
}
