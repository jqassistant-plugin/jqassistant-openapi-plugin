package org.jqassistant.plugin.openapi.impl.jsonschema;

public class QuerryWrapper {

    public static final String REFERENCE_QUERY = "MATCH (n:Schema) WHERE n.name=\"%s\" RETURN n;";

    public static String getSchemaWithName(String refString){
        return String.format(REFERENCE_QUERY, refString.split("/")[3]);
    }
}
