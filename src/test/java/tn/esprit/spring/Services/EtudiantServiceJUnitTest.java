package tn.esprit.spring.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantServiceJUnitTest {

    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        // Initialize the service (without repository interactions)
        etudiantService = new EtudiantService(null); // Passing null as repo won't be used here
    }

    @Test
    void testEtudiantInstantiation() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        assertEquals(1L, etudiant.getId());
    }

    @Test
    void testServiceInstanceNotNull() {
        assertNotNull(etudiantService);
    }
}
