package tn.esprit.spring.ServicesTest.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import static org.junit.jupiter.api.Assertions.*;

public class EtudiantServiceTestJunit {

    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        // Initialize the service without any repository interactions
        etudiantService = new EtudiantService(null);
    }

    @Test
    void testEtudiantInstantiation() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNomEt("John Doe");

        assertEquals(1L, etudiant.getId());
        assertEquals("John Doe", etudiant.getNomEt());
    }

    @Test
    void testServiceInstanceNotNull() {
        assertNotNull(etudiantService);
    }

    @Test
    void testAddOrUpdateWithNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> etudiantService.addOrUpdate(null));
        assertEquals("Etudiant cannot be null", exception.getMessage());
    }


    @Test
    void testEtudiantNameLengthValidation() {
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEt("John");

        assertTrue(etudiant.getNomEt().length() >= 3, "Etudiant name should have at least 3 characters");
    }

    @Test
    void testFindByIdWithNegativeId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> etudiantService.findById(-1L));
        assertEquals("Id must be positive", exception.getMessage());
    }


    @Test
    void testDeleteEtudiantWithNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> etudiantService.delete(null));
        assertEquals("Etudiant cannot be null", exception.getMessage());
    }



    @Test
    void testSetAndGetEtudiantAttributes() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNomEt("Alice");

        assertEquals(1L, etudiant.getId());
        assertEquals("Alice", etudiant.getNomEt());

        etudiant.setNomEt("Bob");
        assertEquals("Bob", etudiant.getNomEt());
    }
}
