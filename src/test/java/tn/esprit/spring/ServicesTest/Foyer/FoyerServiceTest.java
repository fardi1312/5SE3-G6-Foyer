package tn.esprit.spring.ServicesTest.Foyer;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private FoyerService foyerService;

    private Foyer foyer;
    private Universite universite;
    private Bloc bloc;

    @BeforeEach
    public void setUp() {
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer A");

        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Universite A");

        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");
    }

    @Test
    public void testAddOrUpdate() {
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer savedFoyer = foyerService.addOrUpdate(foyer);

        assertNotNull(savedFoyer);
        assertEquals(foyer.getIdFoyer(), savedFoyer.getIdFoyer());
        System.out.println("testAddOrUpdate: Foyer added or updated with ID " + savedFoyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    public void testFindAll() {
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer));

        List<Foyer> foyers = foyerService.findAll();

        assertNotNull(foyers);
        assertEquals(1, foyers.size());
        System.out.println("testFindAll: Found " + foyers.size() + " foyers.");
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer foundFoyer = foyerService.findById(1L);

        assertNotNull(foundFoyer);
        assertEquals(foyer.getIdFoyer(), foundFoyer.getIdFoyer());
        System.out.println("testFindById: Foyer found with ID " + foundFoyer.getIdFoyer());
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> foyerService.findById(1L));

        assertEquals("Foyer with id 1 not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(foyerRepository).deleteById(1L);

        foyerService.deleteById(1L);

        verify(foyerRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteById: Foyer with ID 1 has been deleted.");
    }

    @Test
    public void testDelete() {
        doNothing().when(foyerRepository).delete(foyer);

        foyerService.delete(foyer);

        verify(foyerRepository, times(1)).delete(foyer);
        System.out.println("testDelete: Foyer has been deleted.");
    }

    @Test
    public void testAffecterFoyerAUniversite() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("Universite A")).thenReturn(universite);
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(1L, "Universite A");

        assertNotNull(updatedUniversite);
        assertEquals(universite.getIdUniversite(), updatedUniversite.getIdUniversite());
        assertEquals(foyer, updatedUniversite.getFoyer());
        System.out.println("testAffecterFoyerAUniversite: Foyer has been assigned to Universite A.");
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
    public void testDesaffecterFoyerAUniversite() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite updatedUniversite = foyerService.desaffecterFoyerAUniversite(1L);

        assertNotNull(updatedUniversite);
        assertEquals(universite.getIdUniversite(), updatedUniversite.getIdUniversite());
        assertNull(updatedUniversite.getFoyer());
        System.out.println("testDesaffecterFoyerAUniversite: Foyer has been detached from Universite.");
        verify(universiteRepository, times(1)).save(universite);
    }


}
