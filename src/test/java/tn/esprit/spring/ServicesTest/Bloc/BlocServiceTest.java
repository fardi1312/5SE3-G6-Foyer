package tn.esprit.spring.ServicesTest.Bloc;

import jakarta.persistence.EntityNotFoundException;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private BlocService blocService;

    private List<Bloc> blocs;
    private List<Chambre> chambres;
    private List<Foyer> foyers;

    @BeforeEach
    public void setUp() {
        // Initialiser les données
        blocs = new ArrayList<>();
        chambres = new ArrayList<>();
        foyers = new ArrayList<>();

        // Créer des instances de Bloc
        Bloc blocA = new Bloc();
        blocA.setNomBloc("Bloc A");
        blocA.setChambres(new ArrayList<>());

        Bloc blocB = new Bloc();
        blocB.setNomBloc("Bloc B");
        blocB.setChambres(new ArrayList<>());

        blocs.add(blocA);
        blocs.add(blocB);

        // Créer des instances de Chambre
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        chambres.addAll(Arrays.asList(chambre1, chambre2));

        // Créer une instance de Foyer
        Foyer foyerA = new Foyer();
        foyerA.setNomFoyer("Foyer A");
        foyers.add(foyerA);
    }

    @Test
    public void testAddOrUpdate() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(blocs.get(0));

        Bloc savedBloc = blocService.addOrUpdate(blocs.get(0));

        assertNotNull(savedBloc);
        assertEquals("Bloc A", savedBloc.getNomBloc());
        System.out.println("testAddOrUpdate: " + savedBloc.getNomBloc());  // Affichage du résultat
        verify(blocRepository, times(1)).save(blocs.get(0));
    }

    @Test
    public void testFindAll() {
        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> foundBlocs = blocService.findAll();

        assertNotNull(foundBlocs);
        assertFalse(foundBlocs.isEmpty());
        assertEquals(2, foundBlocs.size());

        System.out.println("testFindAll: ");
        for (Bloc bloc : foundBlocs) {
            System.out.println("Nom du Bloc: " + bloc.getNomBloc());
        }

        verify(blocRepository, times(1)).findAll();
    }


    @Test
    public void testFindById() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(blocs.get(0)));

        Bloc foundBloc = blocService.findById(1L);

        assertNotNull(foundBloc);
        assertEquals("Bloc A", foundBloc.getNomBloc());
        System.out.println("testFindById: " + foundBloc.getNomBloc());  // Affichage du résultat
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(blocRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            blocService.findById(1L);
        });

        assertEquals("Bloc with id 1 not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());  // Affichage du message d'erreur
    }

    @Test
    public void testDeleteById() {
        doNothing().when(blocRepository).deleteById(1L);

        blocService.deleteById(1L);

        verify(blocRepository, times(1)).deleteById(1L);
        System.out.println("testDeleteById: Bloc with id 1 has been deleted.");  // Affichage du résultat
    }

    @Test
    public void testDelete() {
        blocs.get(0).setChambres(chambres); // Associer des chambres au bloc

        doNothing().when(chambreRepository).deleteAll(chambres);
        doNothing().when(blocRepository).delete(blocs.get(0));

        blocService.delete(blocs.get(0));

        verify(chambreRepository, times(1)).deleteAll(chambres);
        verify(blocRepository, times(1)).delete(blocs.get(0));
        System.out.println("testDelete: Bloc has been deleted with its chambres.");  // Affichage du résultat
    }

    @Test
    public void testAffecterBlocAFoyer() {
        Foyer foyer = foyers.get(0);
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(blocs.get(0));
        when(foyerRepository.findByNomFoyer("Foyer A")).thenReturn(foyer);
        when(blocRepository.save(blocs.get(0))).thenReturn(blocs.get(0));

        Bloc updatedBloc = blocService.affecterBlocAFoyer("Bloc A", "Foyer A");

        assertNotNull(updatedBloc);
        assertEquals("Bloc A", updatedBloc.getNomBloc());
        assertEquals(foyer, updatedBloc.getFoyer());
        System.out.println("testAffecterBlocAFoyer: Bloc A has been assigned to Foyer A.");  // Affichage du résultat
        verify(blocRepository, times(1)).save(blocs.get(0));
    }
}
