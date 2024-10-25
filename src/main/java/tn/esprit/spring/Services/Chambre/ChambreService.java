package tn.esprit.spring.Services.Chambre;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    ChambreRepository repo;

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
        // Calculate the start and end of the current academic year
        int currentYear = LocalDate.now().getYear();
        LocalDate dateDebutAU = LocalDate.of((LocalDate.now().getMonthValue() <= 7) ? currentYear - 1 : currentYear, 9, 15);
        LocalDate dateFinAU = LocalDate.of((LocalDate.now().getMonthValue() <= 7) ? currentYear : currentYear + 1, 6, 30);

        // Filter and find available rooms based on type and reservation status
        return repo.findAll().stream()
                .filter(c -> c.getTypeC().equals(type) && c.getBloc().getFoyer().getNomFoyer().equals(nomFoyer))
                .filter(c -> availableSpots(c, dateDebutAU, dateFinAU))
                .toList();
    }

    private boolean availableSpots(Chambre chambre, LocalDate dateDebutAU, LocalDate dateFinAU) {
        long reservationsDuringAU = chambre.getReservations().stream()
                .filter(r -> !r.getAnneeUniversitaire().isBefore(dateDebutAU) && !r.getAnneeUniversitaire().isAfter(dateFinAU))
                .count();

        // Check available spots based on room type
        return switch (chambre.getTypeC()) {
            case SIMPLE -> reservationsDuringAU == 0;
            case DOUBLE -> reservationsDuringAU < 2;
            case TRIPLE -> reservationsDuringAU < 3;
        };
    }


}
