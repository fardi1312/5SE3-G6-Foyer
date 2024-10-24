package tn.esprit.spring.Services.Foyer;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    FoyerRepository repo;
    UniversiteRepository universiteRepository;
    BlocRepository blocRepository;
    private static final String FOYER_NOT_FOUND_MESSAGE = "Foyer with id %d not found";

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return repo.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return repo.findAll();
    }
    @Override

    public Foyer findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FOYER_NOT_FOUND_MESSAGE, id)));
    }


    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Foyer f) {
        repo.delete(f);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
        Foyer f = findById(idFoyer); // Child
        Universite u = universiteRepository.findByNomUniversite(nomUniversite); // Parent
        // On affecte le child au parent
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new EntityNotFoundException("Universite with id " + idUniversite + " not found"));
        u.setFoyer(null);
        return universiteRepository.save(u);
    }


    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        // Récuperer la liste des blocs avant de faire l'ajout
        List<Bloc> blocs = foyer.getBlocs();
        // Foyer est le child et universite est parent
        Foyer f = repo.save(foyer);
        Universite u = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new EntityNotFoundException("Universite with id " + idUniversite + " not found"));
        // Foyer est le child et bloc est le parent
        //On affecte le child au parent
        for (Bloc bloc : blocs) {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        }
        u.setFoyer(f);
        return universiteRepository.save(u).getFoyer();
    }



}
