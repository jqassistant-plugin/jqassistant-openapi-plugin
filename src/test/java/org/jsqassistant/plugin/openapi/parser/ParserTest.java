package org.jsqassistant.plugin.openapi.parser;

import com.buschmais.jqassistant.core.test.plugin.AbstractPluginIT;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest extends AbstractPluginIT {
    @Test
    @TestStore(type = TestStore.Type.FILE)
    public void scanTest(){
        OpenAPIV3Parser parser = new OpenAPIV3Parser();
        OpenAPI openApi = parser.read("src/test/resources/example.yaml");

        Paths paths = openApi.getPaths();
        handlePaths(paths);

        Map<String, Schema> schemas = openApi.getComponents().getSchemas();
        handleSchemas(schemas);

        assertThat(paths).isNotEmpty().hasSize(13);
    }

    private static void handlePaths(Paths paths){
        paths.forEach((path, pathProperties) -> {
            System.out.printf("reading Path <%s>%n", path);
        });
    }

    private static void handleSchemas(Map<String, Schema> schemas){
        schemas.forEach((schema, schemaProperties) -> {
            System.out.printf("reading Schema <%s>%n", schema);
        });
    }


}
