package tn.esprit.spring;

import org.junit.jupiter.api.extension.ExtendWith;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        // Initialize a Reservation object for tests
        reservation = Reservation.builder()
                .idReservation("R001")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();
    }

    @Test
    public void testCreateReservation() {
        // Mock the save method of the repository
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Call the service method
        Reservation createdReservation = reservationService.addOrUpdate(reservation);

        // Verify the interaction with the repository and assert the result
        verify(reservationRepository, times(1)).save(reservation);
        assertThat(createdReservation).isNotNull();
        assertThat(createdReservation.getIdReservation()).isEqualTo("R001");
    }

    @Test
    public void testUpdateReservation() {
        // Mock the save method for update
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Update the reservation status
        reservation.setEstValide(false);

        // Call the service method
        Reservation updatedReservation = reservationService.addOrUpdate(reservation);

        // Verify the interaction with the repository and assert the result
        verify(reservationRepository, times(1)).save(reservation);
        assertThat(updatedReservation).isNotNull();
        assertThat(updatedReservation.isEstValide()).isFalse();
    }



    // Additional simple methods (non-assigned)
    @Test
    public void testMethod1() {
        // Non-implemented method for additional testing
    }

    @Test
    public void testMethod2() {
        // Non-implemented method for additional testing
    }

    @Test
    public void testMethod3() {
        // Non-implemented method for additional testing
    }

    @Test
    public void testDeleteReservation() {
        // Do nothing when calling deleteById (mocked behavior)
        doNothing().when(reservationRepository).deleteById("R001");

        // Call the service method
        reservationService.deleteById("R001");

        // Verify the interaction with the repository
        verify(reservationRepository, times(1)).deleteById("R001");
    }
}