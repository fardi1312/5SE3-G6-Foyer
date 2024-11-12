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
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.Services.Chambre.IChambreService;


import java.util.Arrays;
import java.util.List;

@WebMvcTest(ChambreRestController.class) // Charge uniquement le contrôleur spécifié
class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdate() throws Exception {
        Chambre chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)  // Remplacé SINGLE par SIMPLE
                .build();

        when(service.addOrUpdate(any(Chambre.class))).thenReturn(chambre);

        mockMvc.perform(post("/chambre/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101))
                .andExpect(jsonPath("$.typeC").value("SIMPLE"));  // Remplacé SINGLE par SIMPLE
    }

    @Test
    void testFindAll() throws Exception {
        List<Chambre> chambres = Arrays.asList(
                Chambre.builder().numeroChambre(101).typeC(TypeChambre.SIMPLE).build(),  // Remplacé SINGLE par SIMPLE
                Chambre.builder().numeroChambre(102).typeC(TypeChambre.DOUBLE).build()
        );

        when(service.findAll()).thenReturn(chambres);

        mockMvc.perform(get("/chambre/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroChambre").value(101))
                .andExpect(jsonPath("$[1].numeroChambre").value(102));
    }

    @Test
    void testFindById() throws Exception {
        Chambre chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)  // Remplacé SINGLE par SIMPLE
                .build();

        when(service.findById(1L)).thenReturn(chambre);

        mockMvc.perform(get("/chambre/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101))
                .andExpect(jsonPath("$.typeC").value("SIMPLE"));  // Remplacé SINGLE par SIMPLE
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/chambre/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    void testGetChambresParNomBloc() throws Exception {
        List<Chambre> chambres = Arrays.asList(
                Chambre.builder().numeroChambre(101).typeC(TypeChambre.SIMPLE).build(),  // Remplacé SINGLE par SIMPLE
                Chambre.builder().numeroChambre(102).typeC(TypeChambre.DOUBLE).build()
        );

        when(service.getChambresParNomBloc("Bloc A")).thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresParNomBloc").param("nomBloc", "Bloc A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroChambre").value(101))
                .andExpect(jsonPath("$[1].numeroChambre").value(102));
    }

    @Test
    void testNbChambreParTypeEtBloc() throws Exception {
        when(service.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L)).thenReturn(5L);  // Remplacé SINGLE par SIMPLE

        mockMvc.perform(get("/chambre/nbChambreParTypeEtBloc")
                        .param("type", "SIMPLE")  // Remplacé SINGLE par SIMPLE
                        .param("idBloc", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() throws Exception {
        List<Chambre> chambres = Arrays.asList(
                Chambre.builder().numeroChambre(103).typeC(TypeChambre.SIMPLE).build(),  // Remplacé SINGLE par SIMPLE
                Chambre.builder().numeroChambre(104).typeC(TypeChambre.SIMPLE).build()   // Remplacé SINGLE par SIMPLE
        );

        when(service.getChambresNonReserveParNomFoyerEtTypeChambre("Foyer Central", TypeChambre.SIMPLE))  // Remplacé SINGLE par SIMPLE
                .thenReturn(chambres);

        mockMvc.perform(get("/chambre/getChambresNonReserveParNomFoyerEtTypeChambre")
                        .param("nomFoyer", "Foyer Central")
                        .param("type", "SIMPLE"))  // Remplacé SINGLE par SIMPLE
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroChambre").value(103))
                .andExpect(jsonPath("$[1].numeroChambre").value(104));
    }
}
