package tn.esprit.spring.Services.Etudiant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceMockTest {

    @Mock
    private EtudiantRepository etudiantRepository; // Mock the EtudiantRepository dependency

    @InjectMocks
    private EtudiantService etudiantService; // Inject mocks into the EtudiantService

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Initialize the Etudiant object
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("John Doe");
    }

    @Test
    void testEtudiantInstantiation() {
        // Create a new Etudiant instance
        Etudiant etudiantTest = new Etudiant();
        etudiantTest.setIdEtudiant(1L);
        etudiantTest.setNomEt("John Doe");

        // Assert that the etudiantTest instance is correctly initialized
        assertEquals(1L, etudiantTest.getIdEtudiant());
        assertEquals("John Doe", etudiantTest.getNomEt());
    }

    @Test
    void testServiceInstanceNotNull() {
        // Assert that the EtudiantService instance is correctly injected
        assertNotNull(etudiantService);
    }



    @Test
    void testAddOrUpdate() {
        // Arrange: Mock the behavior of etudiantRepository.save
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Act: Call the service method to add or update Etudiant
        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        // Assert: Verify that the Etudiant was saved and the result matches expectations
        assertNotNull(savedEtudiant);
        assertEquals(1L, savedEtudiant.getIdEtudiant());
        assertEquals("John Doe", savedEtudiant.getNomEt());

        verify(etudiantRepository, times(1)).save(etudiant); // Verify save was called once
    }

    @Test
    void testFindByIdWithNegativeId() {
        // Act & Assert: Ensure an exception is thrown for invalid (negative) Etudiant ID
        Exception exception = assertThrows(IllegalArgumentException.class, () -> etudiantService.findById(-1L));
        assertEquals("Id must be positive", exception.getMessage());
    }

    @Test
    void testFindById() {
        // Arrange: Mock the behavior of etudiantRepository.findById
        when(etudiantRepository.findById(anyLong())).thenReturn(java.util.Optional.of(etudiant));

        // Act: Call the service method to find an Etudiant by ID
        Etudiant foundEtudiant = etudiantService.findById(etudiant.getIdEtudiant());

        // Assert: Verify that the correct Etudiant was found
        assertNotNull(foundEtudiant);
        assertEquals(1L, foundEtudiant.getIdEtudiant());
        assertEquals("John Doe", foundEtudiant.getNomEt());

        verify(etudiantRepository, times(1)).findById(etudiant.getIdEtudiant()); // Verify findById was called
    }

    @Test
    void testFindByIdNotFound() {
        // Arrange: Mock the behavior for a non-existent Etudiant
        when(etudiantRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        // Act & Assert: Ensure an exception is thrown when the Etudiant is not found
        Exception exception = assertThrows(RuntimeException.class, () -> etudiantService.findById(999L));
        assertTrue(exception.getMessage().contains("Etudiant not found"));
    }

    @Test
    void testDeleteEtudiantWithNull() {
        // Act & Assert: Ensure an exception is thrown when attempting to delete a null Etudiant
        Exception exception = assertThrows(IllegalArgumentException.class, () -> etudiantService.delete(null));
        assertEquals("Etudiant cannot be null", exception.getMessage());
    }

    @Test
    void testDeleteEtudiant() {
        // Arrange: Mock the behavior of etudiantRepository.delete
        doNothing().when(etudiantRepository).delete(any(Etudiant.class));

        // Act: Call the service method to delete an Etudiant
        etudiantService.delete(etudiant);

        // Assert: Verify that the delete method was called exactly once
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void testSetAndGetEtudiantAttributes() {
        // Set attributes for Etudiant
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Alice");

        // Assert that the Etudiant's attributes are correctly set
        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Alice", etudiant.getNomEt());

        // Update the attributes and verify again
        etudiant.setNomEt("Bob");
        assertEquals("Bob", etudiant.getNomEt());
    }
}
