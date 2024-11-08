package tn.esprit.spring.Services.Bloc;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BlocService implements IBlocService {
    BlocRepository repo;
    ChambreRepository chambreRepository;
    BlocRepository blocRepository;
    FoyerRepository foyerRepository;

    @Transactional
    public Bloc addOrUpdate(Bloc bloc) {
        // Assuming you're modifying the list of chambres inside the bloc entity
        List<Chambre> chambres = new ArrayList<>(bloc.getChambres());  // Create a copy
        // Proceed with your logic to update the bloc
        bloc.setChambres(chambres);
        return blocRepository.save(bloc);
    }



    @Override
    public List<Bloc> findAll() {
        return repo.findAll();
    }

    @Override
    public Bloc findById(long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Bloc not found with id " + id ));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Bloc b) {
        List<Chambre> chambres= b.getChambres();
        for (Chambre chambre: chambres) {
            chambreRepository.delete(chambre);
        }
        repo.delete(b);
    }

    @Override
    public Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc) {
        //1
        Bloc b = repo.findByNomBloc(nomBloc);
        List<Chambre> chambres= new ArrayList<>();
        for (Long nu: numChambre) {
            Chambre chambre=chambreRepository.findByNumeroChambre(nu);
            chambres.add(chambre);
        }
        for (Chambre cha : chambres) {
            //3 On affecte le child au parent
            cha.setBloc(b);
            //4 save du parent
            chambreRepository.save(cha);
        }
        return b;
    }

    @Override
    public Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer) {
        Bloc b = blocRepository.findByNomBloc(nomBloc); //Parent
        Foyer f = foyerRepository.findByNomFoyer(nomFoyer); //Child
        //On affecte le child au parent
        b.setFoyer(f);
        return blocRepository.save(b);
    }
}