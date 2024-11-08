package tn.esprit.spring.Services.Universite;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UniversiteServiceTest {

    @Autowired
    private UniversiteService universiteService;

    @Autowired
    private UniversiteRepository universiteRepository;

    private Universite universite;

    @BeforeEach
    void setUp() {
        // Create a new Universite entity to be used for tests
        universite = Universite.builder()
                .nomUniversite("Test University")
                .adresse("123 Test St, Test City")
                .build();
    }

    @Test
    void testAddOrUpdate() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotEquals(0L, savedUniversite.getIdUniversite(), "ID should not be zero after save");
        assertEquals("Test University", savedUniversite.getNomUniversite(), "University name should match");
    }

    @Test
    void testFindAll() {
        universiteService.addOrUpdate(universite); // First save the universite
        List<Universite> universites = universiteService.findAll();

        assertFalse(universites.isEmpty(), "Universities list should not be empty");
        assertEquals(1, universites.size(), "There should be one university in the list");
    }

    @Test
    void testFindById() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);
        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());

        assertNotNull(foundUniversite, "University should be found by ID");
        assertEquals(savedUniversite.getIdUniversite(), foundUniversite.getIdUniversite(), "The found university ID should match");
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(999L); // Trying to find an ID that doesn't exist
        }, "EntityNotFoundException should be thrown when university is not found");
    }

    @Test
    void testDeleteById() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);
        long id = savedUniversite.getIdUniversite();

        universiteService.deleteById(id);

        assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(id); // Trying to find a deleted university
        }, "EntityNotFoundException should be thrown after deletion");
    }

    @Test
    void testDelete() {
        // Create and save a Universite entity
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        // Delete the saved university
        universiteService.delete(savedUniversite);

        // Use stream to check for EntityNotFoundException by streaming the result of findById
        assertTrue(
                Stream.of(savedUniversite.getIdUniversite())
                        .map(id -> {
                            try {
                                universiteService.findById(id);
                                return false; // If no exception, return false
                            } catch (EntityNotFoundException e) {
                                return true; // If exception occurs, return true
                            }
                        })
                        .anyMatch(Boolean::booleanValue),
                "EntityNotFoundException should be thrown after deletion"
        );
    }

}
