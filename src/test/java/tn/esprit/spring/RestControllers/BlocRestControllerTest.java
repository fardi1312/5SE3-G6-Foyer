package tn.esprit.spring.RestControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.Services.Bloc.IBlocService;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(BlocRestController.class) // Charge uniquement le contrôleur spécifié
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService service; // Injecte un mock du service

    @Autowired
    private ObjectMapper objectMapper; // Pour la sérialisation JSON

    @Test
    void testAddOrUpdate() throws Exception {
        // Créer un objet Foyer et des chambres pour les tests
        Foyer foyer = new Foyer(); // Ajoutez ici les attributs nécessaires si besoin
        Chambre chambre1 = new Chambre(); // Configurez les chambres si nécessaire
        Chambre chambre2 = new Chambre();

        // Créer un Bloc avec des chambres et un foyer
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer)
                .chambres(Arrays.asList(chambre1, chambre2))
                .build();

        when(service.addOrUpdate(any(Bloc.class))).thenReturn(bloc);

        mockMvc.perform(post("/bloc/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$.capaciteBloc").value(100));
    }

    @Test
    void testFindAll() throws Exception {
        Foyer foyer = new Foyer();
        List<Bloc> blocs = Arrays.asList(
                Bloc.builder().nomBloc("Bloc A").capaciteBloc(100).foyer(foyer).build(),
                Bloc.builder().nomBloc("Bloc B").capaciteBloc(200).foyer(foyer).build()
        );

        when(service.findAll()).thenReturn(blocs);

        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$[1].nomBloc").value("Bloc B"));
    }

    @Test
    void testFindById() throws Exception {
        Foyer foyer = new Foyer();
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer)
                .build();

        when(service.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/bloc/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$.capaciteBloc").value(100));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/bloc/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    void testAffecterChambresABloc() throws Exception {
        List<Long> numChambres = Arrays.asList(1L, 2L, 3L);
        Bloc bloc = Bloc.builder().nomBloc("Bloc A").build();

        when(service.affecterChambresABloc(numChambres, "Bloc A")).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterChambresABloc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(numChambres))
                        .param("nomBloc", "Bloc A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void testAffecterBlocAFoyer() throws Exception {
        Bloc bloc = Bloc.builder().nomBloc("Bloc A").build();

        when(service.affecterBlocAFoyer("Bloc A", "Foyer Central")).thenReturn(bloc);

        mockMvc.perform(put("/bloc/affecterBlocAFoyer")
                        .param("nomBloc", "Bloc A")
                        .param("nomFoyer", "Foyer Central"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }
}

