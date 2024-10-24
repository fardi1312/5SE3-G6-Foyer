package tn.esprit.spring.Services.Universite;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteService implements IUniversiteService {
    UniversiteRepository repo;
    private static final String UNIVERSITE_NOT_FOUND_MESSAGE = "Universite with id %d not found";

    @Override
    public Universite addOrUpdate(Universite u) {
        return repo.save(u);
    }

    @Override
    public List<Universite> findAll() {
        return repo.findAll();
    }


    @Override
    public Universite findById(long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(UNIVERSITE_NOT_FOUND_MESSAGE, id)));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Universite u) {
        repo.delete(u);
    }
}
