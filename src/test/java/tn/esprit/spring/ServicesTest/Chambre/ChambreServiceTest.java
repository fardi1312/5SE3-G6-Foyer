package tn.esprit.spring.ServicesTest.Chambre;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre1;
    private Chambre chambre2;

    @BeforeEach
    public void setUp() {
        chambre1 = new Chambre();
        chambre1.setIdChambre(1L);
        chambre1.setTypeC(TypeChambre.SIMPLE);

        chambre2 = new Chambre();
        chambre2.setIdChambre(2L);
        chambre2.setTypeC(TypeChambre.DOUBLE);
    }

    @Test
    public void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre1);

        Chambre savedChambre = chambreService.addOrUpdate(chambre1);

        assertNotNull(savedChambre);
        assertEquals(chambre1.getIdChambre(), savedChambre.getIdChambre());
        System.out.println("testAddOrUpdate: Chambre added or updated with ID " + savedChambre.getIdChambre());
        verify(chambreRepository, times(1)).save(chambre1);
    }

    @Test
    public void testFindAll() {
        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        List<Chambre> chambres = chambreService.findAll();

        assertNotNull(chambres);
        assertEquals(2, chambres.size());
        System.out.println("testFindAll: Found " + chambres.size() + " chambres.");
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre1));

        Chambre foundChambre = chambreService.findById(1L);

        assertNotNull(foundChambre);
        assertEquals(chambre1.getIdChambre(), foundChambre.getIdChambre());
        System.out.println("testFindById: Chambre found with ID " + foundChambre.getIdChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> chambreService.findById(1L));

        assertEquals("Chambre with id 1 not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(chambreRepository).deleteById(1L);

        chambreService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteById: Chambre with ID 1 has been deleted.");
    }

    @Test
    public void testDelete() {
        doNothing().when(chambreRepository).delete(chambre1);

        chambreService.delete(chambre1);

        verify(chambreRepository, times(1)).delete(chambre1);
        System.out.println("testDelete: Chambre has been deleted.");
    }

    @Test
    public void testGetChambresParNomBloc() {
        when(chambreRepository.findByBlocNomBloc("Bloc A")).thenReturn(Arrays.asList(chambre1, chambre2));

        List<Chambre> chambres = chambreService.getChambresParNomBloc("Bloc A");

        assertNotNull(chambres);
        assertEquals(2, chambres.size());
        System.out.println("testGetChambresParNomBloc: Found " + chambres.size() + " chambres in Bloc A.");
        verify(chambreRepository, times(1)).findByBlocNomBloc("Bloc A");
    }





    @Test
    public void testGetChambresNonReserveParNomFoyerEtTypeChambre() {
        LocalDate dateDebutAU = LocalDate.of(
                (LocalDate.now().getMonthValue() <= 7) ? LocalDate.now().getYear() - 1 : LocalDate.now().getYear(),
                9,
                15
        );
        LocalDate dateFinAU = LocalDate.of(
                (LocalDate.now().getMonthValue() <= 7) ? LocalDate.now().getYear() : LocalDate.now().getYear() + 1,
                6,
                30
        );

        when(chambreRepository.findAvailableChambres("Foyer A", TypeChambre.SIMPLE, dateDebutAU, dateFinAU))
                .thenReturn(Arrays.asList(chambre1));

        List<Chambre> availableChambres = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("Foyer A", TypeChambre.SIMPLE);

        assertNotNull(availableChambres);
        assertEquals(1, availableChambres.size());
        System.out.println("testGetChambresNonReserveParNomFoyerEtTypeChambre: Found " + availableChambres.size() + " available SINGLE type chambres in Foyer A.");
        verify(chambreRepository, times(1)).findAvailableChambres("Foyer A", TypeChambre.SIMPLE, dateDebutAU, dateFinAU);
    }
}
