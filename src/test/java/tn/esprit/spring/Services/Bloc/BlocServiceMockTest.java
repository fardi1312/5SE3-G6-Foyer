package tn.esprit.spring.Services.Bloc;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlocServiceMockTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    private Bloc testBloc;

    @BeforeEach
    public void setUp() {
        testBloc = new Bloc();
        testBloc.setNomBloc("TestBloc");
    }

    @Test
    void testAddOrUpdateBloc() {
        // Given
        Bloc newBloc = new Bloc();
        newBloc.setNomBloc("NewBloc");

        // Mock the repository's behavior
        when(blocRepository.save(newBloc)).thenReturn(newBloc);

        // When
        Bloc savedBloc = blocService.addOrUpdate(newBloc);

        // Then
        System.out.println("Bloc ajouté ou mis à jour : " + savedBloc.getNomBloc());
        assertNotNull(savedBloc);
        assertEquals("NewBloc", savedBloc.getNomBloc());
        verify(blocRepository, times(1)).save(newBloc);  // Ensure that save was called once
    }

    @Test
    void testFindBlocById() {
        // Given
        when(blocRepository.findById(testBloc.getIdBloc())).thenReturn(Optional.of(testBloc));

        // When
        Bloc foundBloc = blocService.findById(testBloc.getIdBloc());

        // Then
        System.out.println("Bloc trouvé : " + foundBloc.getNomBloc());
        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
        verify(blocRepository, times(1)).findById(testBloc.getIdBloc());  // Ensure that findById was called once
    }

    @Test
    void testDeleteBloc() {
        // Given
        when(blocRepository.existsById(testBloc.getIdBloc())).thenReturn(true);

        // When
        blocService.deleteById(testBloc.getIdBloc());

        // Then
        System.out.println("Bloc supprimé : " + !blocRepository.existsById(testBloc.getIdBloc()));
        verify(blocRepository, times(1)).deleteById(testBloc.getIdBloc());  // Ensure that deleteById was called once
        verify(blocRepository, times(1)).existsById(testBloc.getIdBloc());  // Ensure that existsById was called once
    }

    @Test
    void testFindAllBlocs() {
        // Given
        when(blocRepository.findAll()).thenReturn(List.of(testBloc));

        // When
        var blocs = blocService.findAll();

        // Then
        System.out.println("Blocs trouvés : ");
        for (Bloc bloc : blocs) {
            System.out.println("  - " + bloc.getNomBloc());
        }

        assertNotNull(blocs);
        assertFalse(blocs.isEmpty());
        assertEquals(1, blocs.size());  // We mocked a single Bloc
        verify(blocRepository, times(1)).findAll();  // Ensure that findAll was called once
    }
}