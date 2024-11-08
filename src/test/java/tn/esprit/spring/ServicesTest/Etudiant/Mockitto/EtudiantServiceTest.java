package tn.esprit.spring.ServicesTest.Etudiant.Mockitto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdateEtudiant_Success() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        etudiant.setNomEt("Test Student");

        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);
        assertNotNull(savedEtudiant);
        assertEquals(1L, savedEtudiant.getId());
        assertEquals("Test Student", savedEtudiant.getNomEt());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testFindAllEtudiants() {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiants = List.of(etudiant1, etudiant2);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> foundEtudiants = etudiantService.findAll();
        assertEquals(2, foundEtudiants.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testFindEtudiantById_Success() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.findById(1L);
        assertNotNull(foundEtudiant);
        assertEquals(1L, foundEtudiant.getId());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testFindEtudiantById_NotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> etudiantService.findById(1L));
        assertEquals("Etudiant not found", exception.getMessage());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteEtudiantById() {
        long id = 1L;
        etudiantService.deleteById(id);
        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiantService.delete(etudiant);
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    void testUpdateNonExistentEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        Etudiant updatedEtudiant = etudiantService.addOrUpdate(etudiant);
        assertNull(updatedEtudiant);
        verify(etudiantRepository, times(1)).save(etudiant);
    }
}
