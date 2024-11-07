package tn.esprit.spring.ServicesTest.Foyer;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoyerServiceImplMockTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerService foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Test Foyer");

        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addOrUpdate(foyer);

        assertNotNull(result);
        assertEquals("Test Foyer", result.getNomFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testFindAll() {
        List<Foyer> foyers = new ArrayList<>();
        foyers.add(new Foyer());
        foyers.add(new Foyer());

        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.findAll();

        assertEquals(2, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        long idFoyer = 1L;
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(idFoyer);

        when(foyerRepository.findById(idFoyer)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(idFoyer);

        assertNotNull(result);
        assertEquals(idFoyer, result.getIdFoyer());
        verify(foyerRepository, times(1)).findById(idFoyer);
    }

    @Test
    void testDeleteById() {
        long idFoyer = 1L;

        doNothing().when(foyerRepository).deleteById(idFoyer);

        foyerService.deleteById(idFoyer);

        verify(foyerRepository, times(1)).deleteById(idFoyer);
    }

    @Test
    void testDelete() {
        Foyer foyer = new Foyer();

        doNothing().when(foyerRepository).delete(foyer);

        foyerService.delete(foyer);

        verify(foyerRepository, times(1)).delete(foyer);
    }
}
