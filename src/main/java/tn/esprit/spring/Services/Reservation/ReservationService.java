package tn.esprit.spring.Services.Reservation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    ReservationRepository repo;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;
    private static final String RESERVATION_NOT_FOUND_MESSAGE = "Reservation with id %s not found";

    @Override
    public Reservation addOrUpdate(Reservation r) {
        return repo.save(r);
    }

    @Override
    public List<Reservation> findAll() {
        return repo.findAll();
    }


    @Override
    public Reservation findById(String id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(RESERVATION_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Reservation r) {
        repo.delete(r);
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        // Récupération de l'année universitaire actuelle
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        // Initialiser la réservation
        Reservation res = new Reservation();
        Chambre c = chambreRepository.findByNumeroChambre(numChambre);
        Etudiant e = etudiantRepository.findByCin(cin);
        boolean ajout = false;

        // Nombre de réservations dans cette chambre durant l'année universitaire en cours
        int numRes = chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(c.getIdChambre(), dateDebutAU, dateFinAU);
        log.debug("Nombre de réservations pour la chambre {}: {}", c.getNumeroChambre(), numRes);  // Logging the number of reservations

        // Vérifier si la chambre peut accepter de nouvelles réservations en fonction du type
        switch (c.getTypeC()) {
            case SIMPLE:
                if (numRes < 1) {
                    ajout = true;
                } else {
                    log.info("Chambre simple remplie !");
                }
                break;
            case DOUBLE:
                if (numRes < 2) {
                    ajout = true;
                } else {
                    log.info("Chambre double remplie !");
                }
                break;
            case TRIPLE:
                if (numRes < 3) {
                    ajout = true;
                } else {
                    log.info("Chambre triple remplie !");
                }
                break;
        }

        // Ajouter la réservation si la capacité n'est pas atteinte
        if (ajout) {
            res.setEstValide(false);  // La réservation est non validée au départ
            res.setAnneeUniversitaire(LocalDate.now());
            res.setIdReservation(dateDebutAU.getYear() + "/" + dateFinAU.getYear() + "-" + c.getBloc().getNomBloc() + "-" + c.getNumeroChambre() + "-" + e.getCin());
            res.getEtudiants().add(e);  // Assigner l'étudiant
            res.setEstValide(true);  // Valider la réservation

            // Sauvegarder la réservation et mettre à jour la chambre
            res = repo.save(res);
            c.getReservations().add(res);
            chambreRepository.save(c);
            log.info("Réservation ajoutée avec succès pour la chambre {} et l'étudiant {}.", c.getNumeroChambre(), e.getCin());
        } else {
            log.warn("Aucune réservation n'a été ajoutée pour la chambre {}.", c.getNumeroChambre());
        }

        return res;
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cin) {
        Reservation reservation = repo.findByEtudiantsCinAndEstValide(cin, true);
        if (reservation == null) {
            throw new EntityNotFoundException("Reservation not found for student with CIN " + cin);
        }

        Chambre chambre = chambreRepository.findByReservationsIdReservation(reservation.getIdReservation());
        if (chambre != null) {
            List<Reservation> reservations = new ArrayList<>(chambre.getReservations());
            reservations.removeIf(r -> r.getIdReservation().equals(reservation.getIdReservation()));
            chambre.setReservations(reservations);
            chambreRepository.save(chambre);
        }

        repo.delete(reservation);
        return "La réservation " + reservation.getIdReservation() + " est annulée avec succès";
    }



}
