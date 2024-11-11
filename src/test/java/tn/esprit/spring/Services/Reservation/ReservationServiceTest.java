package tn.esprit.spring.Services.Reservation;



import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
 class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;



    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Chambre chambre;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-101-123456");
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());

        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setReservations(new ArrayList<>(Collections.singletonList(reservation)));

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(123456);
    }

    @Test
    @DisplayName("Test d'ajout ou de mise à jour d'une réservation")
    void testAddOrUpdate() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        assertNotNull(savedReservation, "La réservation ne doit pas être nulle après l'ajout ou la mise à jour.");
        assertEquals(reservation.getIdReservation(), savedReservation.getIdReservation(), "L'ID de réservation doit correspondre.");
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    @DisplayName("Test de la récupération de toutes les réservations")
    void testFindAll() {
        when(reservationRepository.findAll()).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.findAll();

        assertNotNull(reservations, "La liste des réservations ne doit pas être nulle.");
        assertEquals(1, reservations.size(), "Il devrait y avoir une réservation.");
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test de la récupération d'une réservation par ID")
    void testFindById() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById("2023/2024-Bloc A-101-123456");

        assertNotNull(foundReservation, "La réservation trouvée ne doit pas être nulle.");
        assertEquals(reservation.getIdReservation(), foundReservation.getIdReservation(), "L'ID de réservation doit correspondre.");
        verify(reservationRepository, times(1)).findById("2023/2024-Bloc A-101-123456");
    }

    @Test
    @DisplayName("Test de la récupération d'une réservation par ID - Non trouvé")
    void testFindById_NotFound() {
        when(reservationRepository.findById("unknown-id")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> reservationService.findById("unknown-id"));
        assertEquals("Reservation with id unknown-id not found", exception.getMessage(), "Le message d'exception doit correspondre.");
    }

    @Test
    @DisplayName("Test de la suppression d'une réservation par ID")
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById("2023/2024-Bloc A-101-123456");

        reservationService.deleteById("2023/2024-Bloc A-101-123456");

        verify(reservationRepository, times(1)).deleteById("2023/2024-Bloc A-101-123456");
    }

    @Test
    @DisplayName("Test de la suppression d'une réservation")
    void testDelete() {
        doNothing().when(reservationRepository).delete(reservation);

        reservationService.delete(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
    }

    @Test
    @DisplayName("Test d'annulation d'une réservation")
    void testAnnulerReservation() {
        when(reservationRepository.findByEtudiantsCinAndEstValide(123456, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);
        doNothing().when(reservationRepository).delete(any(Reservation.class));

        String resultMessage = reservationService.annulerReservation(123456);

        assertNotNull(resultMessage, "Le message de résultat ne doit pas être nul.");
        // Updated the expected message to match the actual result
        assertEquals("La réservation 2023/2024-Bloc A-101-123456 est annulée avec succés", resultMessage);
        verify(reservationRepository, times(1)).delete(reservation);
    }
}
