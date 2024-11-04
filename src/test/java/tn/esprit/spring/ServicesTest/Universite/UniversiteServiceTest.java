package tn.esprit.spring.ServicesTest.Universite;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    private Universite universite;

    @BeforeEach
    public void setUp() {
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Test University");
    }

    @Test
    public void testAddOrUpdate() {
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite savedUniversite = universiteService.addOrUpdate(universite);

        assertNotNull(savedUniversite);
        assertEquals(universite.getNomUniversite(), savedUniversite.getNomUniversite());
        System.out.println("testAddOrUpdate: Universite added or updated with name " + savedUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testFindAll() {
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite));

        List<Universite> universites = universiteService.findAll();

        assertNotNull(universites);
        assertEquals(1, universites.size());
        System.out.println("testFindAll: Found " + universites.size() + " universites.");
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        Universite foundUniversite = universiteService.findById(1L);

        assertNotNull(foundUniversite);
        assertEquals(universite.getNomUniversite(), foundUniversite.getNomUniversite());
        System.out.println("testFindById: Universite found with name " + foundUniversite.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(universiteRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> universiteService.findById(999L));

        assertEquals("Universite with id 999 not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(universiteRepository).deleteById(1L);

        universiteService.deleteById(1L);

        verify(universiteRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteById: Universite with ID 1 has been deleted.");
    }

    @Test
    public void testDelete() {
        doNothing().when(universiteRepository).delete(universite);

        universiteService.delete(universite);

        verify(universiteRepository, times(1)).delete(universite);
        System.out.println("testDelete: Universite has been deleted.");
    }
}
