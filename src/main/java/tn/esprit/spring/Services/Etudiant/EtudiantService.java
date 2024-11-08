package tn.esprit.spring.Services.Etudiant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantService implements IEtudiantService {
    private final EtudiantRepository repo;

    @Override
    public Etudiant addOrUpdate(Etudiant e) {
        if (e == null) {
            throw new IllegalArgumentException("Etudiant cannot be null");
        }
        return repo.save(e);
    }


    @Override
    public List<Etudiant> findAll() {
        return repo.findAll();
    }

    @Override
    public Etudiant findById(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be positive");
        }
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Etudiant not found"));
    }


    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Etudiant e) {
        if (e == null) {
            throw new IllegalArgumentException("Etudiant cannot be null");
        }
        repo.delete(e);
    }
}
