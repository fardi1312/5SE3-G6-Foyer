package tn.esprit.spring.Services.Chambre;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Reset context after each test
@Transactional
class ChambreServiceTest {

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private ChambreRepository chambreRepository;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        // Initialisation d'un objet Chambre avec les valeurs nécessaires
        Bloc bloc = new Bloc(0, "Bloc A", 100, null, null);
        chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)
                .bloc(bloc)
                .build();

        // Sauvegarder dans la base de données pour que les tests puissent l'utiliser
        chambreRepository.save(chambre);
    }

    @Test
    void testAddOrUpdateChambre() {
        // Mise à jour de la chambre existante
        chambre.setNumeroChambre(102);
        Chambre updatedChambre = chambreService.addOrUpdate(chambre);

        assertNotNull(updatedChambre);
        assertEquals(102, updatedChambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, updatedChambre.getTypeC());
    }



    @Test
    void testFindByIdChambre() {
        Chambre foundChambre = chambreService.findById(chambre.getIdChambre());

        assertNotNull(foundChambre);
        assertEquals(101, foundChambre.getNumeroChambre());
    }

    @Test
    void testFindByIdChambreNotFound() {
        long nonExistentId = 999L;
        Exception exception = assertThrows(Exception.class, () -> {
            chambreService.findById(nonExistentId);
        });

        String expectedMessage = "Chambre with id " + nonExistentId + " not found";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testDeleteByIdChambre() {
        chambreService.deleteById(chambre.getIdChambre());

        Optional<Chambre> deletedChambre = chambreRepository.findById(chambre.getIdChambre());
        assertFalse(deletedChambre.isPresent());
    }

    @Test
    void testDeleteChambre() {
        chambreService.delete(chambre);

        Optional<Chambre> deletedChambre = chambreRepository.findById(chambre.getIdChambre());
        assertFalse(deletedChambre.isPresent());
    }

    @Test
    void testGetChambresParNomBloc() {
        List<Chambre> chambres = chambreService.getChambresParNomBloc("Bloc A");

        assertNotNull(chambres);
        assertFalse(chambres.isEmpty());
        assertEquals("Bloc A", chambres.get(0).getBloc().getNomBloc());
    }

    @Test
    void testNbChambreParTypeEtBloc() {
        long count = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, chambre.getBloc().getIdBloc());

        assertEquals(1L, count);
    }
}
