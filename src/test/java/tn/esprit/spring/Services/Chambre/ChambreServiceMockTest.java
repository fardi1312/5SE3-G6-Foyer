package tn.esprit.spring.Services.Chambre;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class ChambreServiceMockTest {

    @Mock
    private ChambreRepository chambreRepository; // Mock the ChambreRepository dependency

    @InjectMocks
    private ChambreService chambreService; // Inject mocks into the ChambreService

    private Chambre chambre;

    @BeforeEach
    public void setUp() {
        // Setup Chambre object
        Bloc bloc = new Bloc(0, "Bloc A", 100, null, null);
        chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)
                .bloc(bloc)
                .build();
    }

    @Test
    void testAddOrUpdateChambre() {
        // Arrange: mock the behavior of chambreRepository.save
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        // Act: call the service method
        Chambre updatedChambre = chambreService.addOrUpdate(chambre);

        // Assert: verify that the mock method was called and the response is as expected
        assertNotNull(updatedChambre);
        assertEquals(101, updatedChambre.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre); // Verify save was called exactly once
    }

    @Test
    void testFindByIdChambre() {
        // Arrange: mock the behavior of chambreRepository.findById
        when(chambreRepository.findById(anyLong())).thenReturn(java.util.Optional.of(chambre));

        // Act: call the service method
        Chambre foundChambre = chambreService.findById(chambre.getIdChambre());

        // Assert: verify the chambre was found
        assertNotNull(foundChambre);
        assertEquals(101, foundChambre.getNumeroChambre());
        verify(chambreRepository, times(1)).findById(chambre.getIdChambre()); // Verify findById was called
    }

    @Test
    void testFindByIdChambreNotFound() {
        // Arrange: mock the behavior for a non-existent chambre
        when(chambreRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        // Act & Assert: ensure an exception is thrown
        Exception exception = assertThrows(Exception.class, () -> {
            chambreService.findById(999L);
        });
        assertTrue(exception.getMessage().contains("Chambre with id 999 not found"));
    }

    @Test
    void testDeleteChambre() {
        // Arrange: mock the behavior of chambreRepository.delete
        doNothing().when(chambreRepository).delete(any(Chambre.class));

        // Act: call the service method
        chambreService.delete(chambre);

        // Assert: verify delete was called once
        verify(chambreRepository, times(1)).delete(chambre);
    }

    @Test
    void testGetChambresParNomBloc() {
        // Arrange: mock the behavior of chambreRepository.findByBlocNomBloc
        when(chambreRepository.findByBlocNomBloc(anyString())).thenReturn(java.util.List.of(chambre));

        // Act: call the service method
        var chambres = chambreService.getChambresParNomBloc("Bloc A");

        // Assert: verify the chambres returned are as expected
        assertNotNull(chambres);
        assertFalse(chambres.isEmpty());
        assertEquals("Bloc A", chambres.get(0).getBloc().getNomBloc());
        verify(chambreRepository, times(1)).findByBlocNomBloc("Bloc A"); // Verify findByBlocNomBloc was called
    }

}
