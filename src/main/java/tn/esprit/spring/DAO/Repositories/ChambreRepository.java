package tn.esprit.spring.DAO.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.time.LocalDate;
import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {
    Chambre findByNumeroChambre(long num);

    List<Chambre> findByBlocNomBloc(String nom);

    int countByTypeCAndBlocIdBloc(TypeChambre typeChambre, long idBloc);


    int countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(long chambreId, LocalDate dateDebutAU, LocalDate dateFinAU);

    //*****************************************************************
    Chambre findByReservationsIdReservation(String idReservation);

    long count();



    @Query("SELECT c FROM Chambre c WHERE c.bloc.foyer.nomFoyer = :nomFoyer AND c.typeC = :typeChambre " +
            "AND NOT EXISTS (SELECT r FROM Reservation r WHERE  r.estValide = true " +
            "AND r.anneeUniversitaire BETWEEN :dateDebut AND :dateFin)")
    List<Chambre> findAvailableChambres(
            @Param("nomFoyer") String nomFoyer,
            @Param("typeChambre") TypeChambre typeChambre,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );



}