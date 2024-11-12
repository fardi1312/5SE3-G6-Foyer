package tn.esprit.spring.RestControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Universite.IUniversiteService;
import tn.esprit.spring.RestControllers.UniversiteRestController;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(UniversiteRestController.class) // Charge uniquement le contrôleur spécifié
class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService service; // Injecte un mock du service

    @Autowired
    private ObjectMapper objectMapper; // Pour la sérialisation JSON

    @Test
    void testAddOrUpdate() throws Exception {
        // Créer des objets Foyer et Universite
        Foyer foyer = new Foyer(); // Ajoutez ici les attributs nécessaires à Foyer si besoin
        Universite universite = Universite.builder()
                .nomUniversite("ESPRIT")
                .adresse("Technopole El Ghazela")
                .foyer(foyer)
                .build();

        when(service.addOrUpdate(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"))
                .andExpect(jsonPath("$.adresse").value("Technopole El Ghazela"));
    }

    @Test
    void testFindAll() throws Exception {
        Foyer foyer1 = new Foyer(); // Ajoutez ici les attributs nécessaires à Foyer si besoin
        Foyer foyer2 = new Foyer(); // Ajoutez ici les attributs nécessaires à Foyer si besoin
        List<Universite> universites = Arrays.asList(
                Universite.builder().nomUniversite("ESPRIT").adresse("Technopole El Ghazela").foyer(foyer1).build(),
                Universite.builder().nomUniversite("ENIT").adresse("Tunis").foyer(foyer2).build()
        );

        when(service.findAll()).thenReturn(universites);

        mockMvc.perform(get("/universite/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniversite").value("ESPRIT"))
                .andExpect(jsonPath("$[1].nomUniversite").value("ENIT"));
    }

    @Test
    void testFindById() throws Exception {
        Foyer foyer = new Foyer(); // Ajoutez ici les attributs nécessaires à Foyer si besoin
        Universite universite = Universite.builder()
                .nomUniversite("ESPRIT")
                .adresse("Technopole El Ghazela")
                .foyer(foyer)
                .build();

        when(service.findById(1L)).thenReturn(universite);

        mockMvc.perform(get("/universite/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"))
                .andExpect(jsonPath("$.adresse").value("Technopole El Ghazela"));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/universite/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }
}
