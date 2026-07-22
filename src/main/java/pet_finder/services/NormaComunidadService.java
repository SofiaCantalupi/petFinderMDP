package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.dtos.norma.NormaComunidadDetailDTO;
import pet_finder.models.NormaComunidad;
import pet_finder.repositories.NormaComunidadRepository;

import java.util.List;

@Service
public class NormaComunidadService {

    private final NormaComunidadRepository repository;

    public NormaComunidadService(NormaComunidadRepository repository) {
        this.repository = repository;
    }

    public List<NormaComunidadDetailDTO> verNormas(){
        return repository.findAll()
                .stream()
                .map(NormaComunidadDetailDTO::new)
                .toList();
    }

    public NormaComunidadDetailDTO crear(NormaComunidad norma){
        return new NormaComunidadDetailDTO(repository.save(norma));
    }
}
