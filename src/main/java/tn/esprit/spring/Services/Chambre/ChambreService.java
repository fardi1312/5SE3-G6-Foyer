package tn.esprit.spring.Services.Chambre;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    ChambreRepository repo;
    BlocRepository blocRepository;
    // Définition de constantes pour éviter la duplication de chaînes de caractères
    private static final String MESSAGE_PLACE_DISPO = "Le nombre de place disponible pour la chambre ";
    private static final String MESSAGE_CHAMBRE_COMPLETE = " est complete";
    private static final String MESSAGE_LA_CHAMBRE = "La chambre ";

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Chambre with id " + id + " not found"));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return repo.countByTypeCAndBlocIdBloc(type, idBloc);
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
        LocalDate[] universityYearDates = calculateUniversityYearDates();
        List<Chambre> availableChambres = new ArrayList<>();

        for (Chambre chambre : repo.findAll()) {
            if (isChambreMatchingFoyerAndType(chambre, nomFoyer, type)) {
                int numReservation = countReservationsInUniversityYear(chambre, universityYearDates);
                if (isChambreAvailable(chambre, numReservation)) {
                    availableChambres.add(chambre);
                }
            }
        }
        return availableChambres;
    }
    // Helper method to calculate university year start and end dates
    private LocalDate[] calculateUniversityYearDates() {
        var dates = new LocalDate[2];
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dates[0] = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dates[1] = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dates[0] = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dates[1] = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        return dates;
    }
    // Helper method to check if a chambre matches the specified foyer and type
    private boolean isChambreMatchingFoyerAndType(Chambre chambre, String nomFoyer, TypeChambre type) {
        return chambre.getTypeC().equals(type) && chambre.getBloc().getFoyer().getNomFoyer().equals(nomFoyer);
    }
    // Helper method to count reservations for a chambre in the current university year
    private int countReservationsInUniversityYear(Chambre chambre, LocalDate[] universityYearDates) {
        var count = 0;
        for (Reservation reservation : chambre.getReservations()) {
            if (reservation.getAnneeUniversitaire().isBefore(universityYearDates[1]) && reservation.getAnneeUniversitaire().isAfter(universityYearDates[0])) {
                count++;
            }
        }
        return count;
    }
    // Helper method to check if a chambre is available based on its type and reservation count
    private boolean isChambreAvailable(Chambre chambre, int numReservation) {
        switch (chambre.getTypeC()) {
            case SIMPLE:
                return numReservation == 0;
            case DOUBLE:
                return numReservation < 2;
            case TRIPLE:
                return numReservation < 3;
            default:
                return false;
        }
    }

    @Override
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (!b.getChambres().isEmpty()) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        var pSimple = (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / (double) totalChambre;
        var pDouble = (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / (double) totalChambre;
        var pTriple = (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / (double) totalChambre;
        log.info("Nombre total des chambre: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égale à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égale à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égale à " + pTriple);

    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
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

        for (Chambre c : repo.findAll()) {
            long nbReservation = repo.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(c.getIdChambre(),true, dateDebutAU, dateFinAU);

            // Simplification du traitement des différents types de chambre
            switch (c.getTypeC()) {
                case SIMPLE:
                    logChambreAvailability(c, nbReservation, 1);
                    break;
                case DOUBLE:
                    logChambreAvailability(c, nbReservation, 2);
                    break;
                case TRIPLE:
                    logChambreAvailability(c, nbReservation, 3);
                    break;
            }
        }
    }
    // Extraction de la logique d'affichage de l'état des chambres dans une méthode dédiée
    private void logChambreAvailability(Chambre c, long nbReservation, int maxPlaces) {
        // Affichage si des places sont disponibles
        if (nbReservation < maxPlaces) {
            log.info(MESSAGE_LA_CHAMBRE + c.getNumeroChambre() + " " + MESSAGE_PLACE_DISPO + c.getTypeC() + " est de " + (maxPlaces - nbReservation) + " place(s) disponible(s)");
        } else {
            // Affichage si la chambre est complète
            log.info(MESSAGE_LA_CHAMBRE + c.getNumeroChambre() + " " + MESSAGE_PLACE_DISPO + c.getTypeC() + MESSAGE_CHAMBRE_COMPLETE);
        }
    }
}
