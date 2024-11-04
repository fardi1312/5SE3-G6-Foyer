package tn.esprit.spring.ServicesTest.Reservation;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Bloc;  // Ensure this import is included
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private Chambre chambre;
    private Etudiant etudiant;

    @BeforeEach
    public void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("2023/2024-Bloc A-101-123456");
        reservation.setEstValide(true);
        reservation.setAnneeUniversitaire(LocalDate.now());

        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        // Initialize the reservations list
        chambre.setReservations(new ArrayList<>());
        chambre.getReservations().add(reservation); // Now you can add reservations

        etudiant = new Etudiant();
        etudiant.setCin(123456);
    }

    @Test
    public void testAddOrUpdate() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addOrUpdate(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation.getIdReservation(), savedReservation.getIdReservation());
        System.out.println("testAddOrUpdate: Reservation added or updated with ID " + savedReservation.getIdReservation());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testFindAll() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationService.findAll();

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        System.out.println("testFindAll: Found " + reservations.size() + " reservations.");
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        when(reservationRepository.findById("2023/2024-Bloc A-101-123456")).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.findById("2023/2024-Bloc A-101-123456");

        assertNotNull(foundReservation);
        assertEquals(reservation.getIdReservation(), foundReservation.getIdReservation());
        System.out.println("testFindById: Reservation found with ID " + foundReservation.getIdReservation());
        verify(reservationRepository, times(1)).findById("2023/2024-Bloc A-101-123456");
    }

    @Test
    public void testFindById_NotFound() {
        when(reservationRepository.findById("unknown-id")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> reservationService.findById("unknown-id"));

        assertEquals("Reservation with id unknown-id not found", exception.getMessage());
        System.out.println("testFindById_NotFound: " + exception.getMessage());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(reservationRepository).deleteById("2023/2024-Bloc A-101-123456");

        reservationService.deleteById("2023/2024-Bloc A-101-123456");

        verify(reservationRepository, times(1)).deleteById("2023/2024-Bloc A-101-123456");
        System.out.println("testDeleteById: Reservation with ID 2023/2024-Bloc A-101-123456 has been deleted.");
    }

    @Test
    public void testDelete() {
        doNothing().when(reservationRepository).delete(reservation);

        reservationService.delete(reservation);

        verify(reservationRepository, times(1)).delete(reservation);
        System.out.println("testDelete: Reservation has been deleted.");
    }


    @Test
    public void testAnnulerReservation() {
        // Arrange
        when(reservationRepository.findByEtudiantsCinAndEstValide(123456, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation(reservation.getIdReservation())).thenReturn(chambre);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        // Stubbing the void method
        doNothing().when(reservationRepository).delete(any(Reservation.class));

        // Act
        String resultMessage = reservationService.annulerReservation(123456);

        // Assert
        assertNotNull(resultMessage);
        assertEquals("La réservation 2023/2024-Bloc A-101-123456 est annulée avec succès", resultMessage);
        System.out.println("testAnnulerReservation: " + resultMessage);

        // Verify
        verify(reservationRepository, times(1)).delete(reservation);
    }
}
