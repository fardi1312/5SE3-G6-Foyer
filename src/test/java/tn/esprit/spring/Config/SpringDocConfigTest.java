package tn.esprit.spring.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SpringDocConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private GroupedOpenApi allPublicApi;

    @Autowired
    private GroupedOpenApi blocPublicApi;

    @Autowired
    private GroupedOpenApi chambrePublicApi;

    @Autowired
    private GroupedOpenApi etudiantPublicApi;

    @Autowired
    private GroupedOpenApi foyerPublicApi;

    @Autowired
    private GroupedOpenApi reservationPublicApi;

    @Autowired
    private GroupedOpenApi universitePublicApi;

    @Test
    void testOpenAPIConfiguration() {
        // Test OpenAPI bean configuration
        assertNotNull(openAPI);
        Info info = openAPI.getInfo();
        assertEquals("Gestion d'un foyer", info.getTitle());
        assertEquals("TP étude de cas 2023-2024", info.getDescription());
        assertEquals("Sirine NAIFAR", info.getContact().getName());
        assertEquals("sirine.naifer@esprit.tn", info.getContact().getEmail());
    }

    @Test
    void testGroupedOpenApis() {
        // Test GroupedOpenApi bean configurations
        assertNotNull(allPublicApi);
        assertTrue(allPublicApi.getPathsToMatch().contains("/**"));
        assertTrue(allPublicApi.getPathsToExclude().contains("**"));

        assertNotNull(blocPublicApi);
        assertTrue(blocPublicApi.getPathsToMatch().contains("/bloc/**"));

        assertNotNull(chambrePublicApi);
        assertTrue(chambrePublicApi.getPathsToMatch().contains("/chambre/**"));

        assertNotNull(etudiantPublicApi);
        assertTrue(etudiantPublicApi.getPathsToMatch().contains("/etudiant/**"));

        assertNotNull(foyerPublicApi);
        assertTrue(foyerPublicApi.getPathsToMatch().contains("/foyer/**"));

        assertNotNull(reservationPublicApi);
        assertTrue(reservationPublicApi.getPathsToMatch().contains("/reservation/**"));

        assertNotNull(universitePublicApi);
        assertTrue(universitePublicApi.getPathsToMatch().contains("/universite/**"));
    }
}

