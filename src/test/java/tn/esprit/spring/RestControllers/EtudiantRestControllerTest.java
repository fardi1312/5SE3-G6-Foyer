package tn.esprit.spring.RestControllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(EtudiantRestController.class)
class EtudiantRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdate() throws Exception {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Dupont")
                .prenomEt("Jean")
                .cin(12345678L)
                .ecole("Université XYZ")
                .dateNaissance(LocalDate.of(1995, 5, 15))
                .build();

        when(service.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1))
                .andExpect(jsonPath("$.nomEt").value("Dupont"))
                .andExpect(jsonPath("$.prenomEt").value("Jean"))
                .andExpect(jsonPath("$.ecole").value("Université XYZ"));
    }

    @Test
    void testFindAll() throws Exception {
        List<Etudiant> etudiants = Arrays.asList(
                Etudiant.builder().idEtudiant(1L).nomEt("Dupont").prenomEt("Jean").ecole("Université XYZ").build(),
                Etudiant.builder().idEtudiant(2L).nomEt("Martin").prenomEt("Pierre").ecole("Université ABC").build()
        );

        when(service.findAll()).thenReturn(etudiants);

        mockMvc.perform(get("/etudiant/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEtudiant").value(1))
                .andExpect(jsonPath("$[1].idEtudiant").value(2));
    }

    @Test
    void testFindById() throws Exception {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Dupont")
                .prenomEt("Jean")
                .cin(12345678L)
                .ecole("Université XYZ")
                .dateNaissance(LocalDate.of(1995, 5, 15))
                .build();

        when(service.findById(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1))
                .andExpect(jsonPath("$.nomEt").value("Dupont"))
                .andExpect(jsonPath("$.prenomEt").value("Jean"));
    }

    @Test
    void testDelete() throws Exception {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Dupont")
                .prenomEt("Jean")
                .ecole("Université XYZ")
                .build();

        // Utilisation d'ArgumentCaptor pour capturer l'argument passé à la méthode delete
        ArgumentCaptor<Etudiant> captor = ArgumentCaptor.forClass(Etudiant.class);

        mockMvc.perform(delete("/etudiant/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk());

        // Vérifier que la méthode delete a été appelée avec l'argument attendu
        verify(service, times(1)).delete(captor.capture());

        // Vérifier que les valeurs sont égales
        Etudiant capturedEtudiant = captor.getValue();
        assertEquals(etudiant.getIdEtudiant(), capturedEtudiant.getIdEtudiant());
        assertEquals(etudiant.getNomEt(), capturedEtudiant.getNomEt());
        assertEquals(etudiant.getPrenomEt(), capturedEtudiant.getPrenomEt());
        assertEquals(etudiant.getEcole(), capturedEtudiant.getEcole());
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/etudiant/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteById(1L);
    }
}
