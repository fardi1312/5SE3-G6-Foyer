package tn.esprit.spring.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_BLOC")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bloc implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBloc;

    private String nomBloc;

    private long capaciteBloc;


    @ManyToOne
    private Foyer foyer;

    @OneToMany(mappedBy = "bloc", fetch = FetchType.EAGER)
    private List<Chambre> chambres = new ArrayList<>(); // No 'transient' here, initialized to avoid nulls

}
