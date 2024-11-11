package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    private Foyer foyer;

    @BeforeEach
    void setUp() {
        // Initialize the foyer object
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);


        // Initialize an empty list for blocs
        List<Bloc> blocs = new ArrayList<>();
        foyer.setBlocs(blocs);

        // Initialize an empty universite object
        Universite universite = new Universite();
        foyer.setUniversite(universite);
    }

    @Test
    void testFoyerAttributes() {
        assertThat(foyer.getIdFoyer()).isEqualTo(1L);
        assertThat(foyer.getNomFoyer()).isEqualTo("Test Foyer");
        assertThat(foyer.getCapaciteFoyer()).isEqualTo(100);

    }

    @Test
    void testEmptyBlocsList() {
        assertThat(foyer.getBlocs()).isEmpty();
    }

    @Test
    void testUniversiteAssociation() {
        assertThat(foyer.getUniversite()).isNotNull();
    }
}