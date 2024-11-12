package tn.esprit.spring.Services.Bloc;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Reset context after each test
@Transactional
class BlocServiceTest {

    @Autowired
    private BlocService blocService;

    @Autowired
    private BlocRepository blocRepository;

    private Bloc testBloc;

    @BeforeEach
    public void setUp() {
        testBloc = new Bloc();
        testBloc.setNomBloc("TestBloc");
        blocRepository.save(testBloc);  // Save an initial Bloc for testing
        System.out.println("Bloc initialisé : " + testBloc.getNomBloc());
    }

    @Test
    void testAddOrUpdateBloc() {
        Bloc newBloc = new Bloc();
        newBloc.setNomBloc("NewBloc");
        Bloc savedBloc = blocService.addOrUpdate(newBloc);

        // Affichage dans la console
        System.out.println("Bloc ajouté ou mis à jour : " + savedBloc.getNomBloc());

        assertNotNull(savedBloc);
        assertEquals("NewBloc", savedBloc.getNomBloc());
        assertTrue(blocRepository.existsById(savedBloc.getIdBloc()));
    }

    @Test
    void testFindAllBlocs() {
        var blocs = blocService.findAll();

        // Affichage dans la console
        System.out.println("Blocs trouvés : ");
        for (Bloc bloc : blocs) {
            System.out.println("  - " + bloc.getNomBloc());
        }

        assertNotNull(blocs);
        assertTrue(blocs.size() > 0, "Expected at least one bloc in the database.");
    }

    @Test
    void testFindBlocById() {
        Bloc foundBloc = blocService.findById(testBloc.getIdBloc());

        // Affichage dans la console
        System.out.println("Bloc trouvé : " + foundBloc.getNomBloc());

        assertNotNull(foundBloc);
        assertEquals("TestBloc", foundBloc.getNomBloc());
    }

    @Test
    void testDeleteBloc() {
        blocService.deleteById(testBloc.getIdBloc());

        // Vérifier si le bloc a été supprimé
        boolean isDeleted = !blocRepository.existsById(testBloc.getIdBloc());
        System.out.println("Bloc supprimé : " + isDeleted);

        assertFalse(blocRepository.existsById(testBloc.getIdBloc()), "The bloc should be deleted.");
    }
}