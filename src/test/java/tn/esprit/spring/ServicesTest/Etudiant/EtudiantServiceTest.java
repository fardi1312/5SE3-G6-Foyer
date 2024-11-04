package tn.esprit.spring.ServicesTest.Etudiant;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant1;
    private Etudiant etudiant2;

    @BeforeEach
    public void setUp() {
        etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1L);
        etudiant1.setNomEt("John Doe");

        etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2L);
        etudiant2.setNomEt("Jane Doe");
    }

    @Test
    public void testAddOrUpdate() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant1);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant1);

        assertNotNull(savedEtudiant);
        assertEquals(etudiant1.getIdEtudiant(), savedEtudiant.getIdEtudiant());
        System.out.println("testAddOrUpdate: Etudiant added or updated with ID " + savedEtudiant.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant1);
    }

    @Test
    public void testFindAll() {
        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant1, etudiant2));

        List<Etudiant> etudiants = etudiantService.findAll();

        assertNotNull(etudiants);
        assertEquals(2, etudiants.size());
        System.out.println("testFindAll: Found " + etudiants.size() + " etudiants.");
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant1));

        Etudiant foundEtudiant = etudiantService.findById(1L);

        assertNotNull(foundEtudiant);
        assertEquals(etudiant1.getIdEtudiant(), foundEtudiant.getIdEtudiant());
        System.out.println("testFindById: Etudiant found with ID " + foundEtudiant.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> etudiantService.findById(1L));

        assertEquals("Etudiant with id 1 not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(etudiantRepository).deleteById(1L);

        etudiantService.deleteById(1L);

        verify(etudiantRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteById: Etudiant with ID 1 has been deleted.");
    }

    @Test
    public void testDelete() {
        doNothing().when(etudiantRepository).delete(etudiant1);

        etudiantService.delete(etudiant1);

        verify(etudiantRepository, times(1)).delete(etudiant1);
        System.out.println("testDelete: Etudiant has been deleted.");
    }
}
