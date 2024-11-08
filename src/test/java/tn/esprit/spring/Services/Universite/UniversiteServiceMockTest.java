package tn.esprit.spring.Services.Universite;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UniversiteServiceMockTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    private Universite universite;

    @BeforeEach
    void setUp() {
        universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("Test University")
                .adresse("123 Test St")
                .build();
    }

    @Test
    void testAddOrUpdate() {
        // Arrange: Mock the save method to return the same Universite object
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Act: Call the method under test
        Universite result = universiteService.addOrUpdate(universite);

        // Assert: Verify that the save method was called and the result is correct
        verify(universiteRepository, times(1)).save(universite);
        assertEquals(universite, result);
    }

    @Test
    void testFindById_ExistingId() {
        // Arrange: Mock the findById method to return the Universite object when the ID is found
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        // Act: Call the method under test
        Universite result = universiteService.findById(1L);

        // Assert: Verify that the Universite was found and the result is correct
        assertEquals(universite, result);
    }

    @Test
    void testFindById_NonExistingId() {
        // Arrange: Mock the findById method to return empty Optional when the ID does not exist
        when(universiteRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert: Verify that the exception is thrown
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            universiteService.findById(2L);
        });

        assertEquals("Universite with id 2 not found", exception.getMessage());
    }

    @Test
    void testDeleteById() {
        // Act: Call the deleteById method
        universiteService.deleteById(1L);

        // Assert: Verify that the deleteById method was called once
        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        // Act: Call the delete method
        universiteService.delete(universite);

        // Assert: Verify that the delete method was called once
        verify(universiteRepository, times(1)).delete(universite);
    }
}
