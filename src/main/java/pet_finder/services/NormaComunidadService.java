package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.models.NormaComunidad;
import pet_finder.repositories.NormaComunidadRepository;

import java.util.List;

@Service
public class NormaComunidadService {
    private final NormaComunidadRepository repository;

    public NormaComunidadService(NormaComunidadRepository repository) {
        this.repository = repository;
    }

    public List<NormaComunidad> verNormas(){
        return repository.findAll();
    }

    public NormaComunidad crear(NormaComunidad norma){
        return repository.save(norma);
    }
}
