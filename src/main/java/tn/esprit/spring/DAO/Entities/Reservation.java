package tn.esprit.spring.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_RESERVATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idReservation;

    String description;

    // Field for the academic year - LocalDate is serializable
    LocalDate anneeUniversitaire;

    // Use private to encapsulate the collection
    @ManyToMany
    private List<Etudiant> etudiants = new ArrayList<>();

    // Method to add an Etudiant to the reservation
    public void addEtudiant(Etudiant etudiant) {
        if (etudiant != null && !etudiants.contains(etudiant)) {
            etudiants.add(etudiant);
            etudiant.getReservations().add(this); // Maintain bidirectional relationship
        }
    }

    // Method to remove an Etudiant from the reservation
    public void removeEtudiant(Etudiant etudiant) {
        if (etudiant != null) {
            etudiants.remove(etudiant);
            etudiant.getReservations().remove(this); // Maintain bidirectional relationship
        }
    }
}
