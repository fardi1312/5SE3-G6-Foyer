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

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class UniversiteServiceTest {

    @Autowired
    private UniversiteService universiteService;

    @Autowired
    private UniversiteRepository universiteRepository;

    private Universite universite;

    @BeforeEach
    public void setUp() {
        // Create a new Universite entity to be used for tests
        universite = Universite.builder()
                .nomUniversite("Test University")
                .adresse("123 Test St, Test City")
                .build();
    }

    @Test
    public void testAddOrUpdate() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotNull(savedUniversite.getIdUniversite(), "ID should not be null after save");
        assertEquals("Test University", savedUniversite.getNomUniversite(), "University name should match");
    }

    @Test
    public void testFindAll() {
        universiteService.addOrUpdate(universite); // First save the universite
        List<Universite> universites = universiteService.findAll();

        assertFalse(universites.isEmpty(), "Universities list should not be empty");
        assertEquals(1, universites.size(), "There should be one university in the list");
    }

    @Test
    public void testFindById() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);
        Universite foundUniversite = universiteService.findById(savedUniversite.getIdUniversite());

        assertNotNull(foundUniversite, "University should be found by ID");
        assertEquals(savedUniversite.getIdUniversite(), foundUniversite.getIdUniversite(), "The found university ID should match");
    }

    @Test
    public void testFindByIdNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(999L); // Trying to find an ID that doesn't exist
        }, "EntityNotFoundException should be thrown when university is not found");
    }

    @Test
    public void testDeleteById() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);
        long id = savedUniversite.getIdUniversite();

        universiteService.deleteById(id);

        assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(id); // Trying to find a deleted university
        }, "EntityNotFoundException should be thrown after deletion");
    }

    @Test
    public void testDelete() {
        Universite savedUniversite = universiteService.addOrUpdate(universite);

        universiteService.delete(savedUniversite);

        assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(savedUniversite.getIdUniversite()); // Trying to find a deleted university
        }, "EntityNotFoundException should be thrown after deletion");
    }
}
