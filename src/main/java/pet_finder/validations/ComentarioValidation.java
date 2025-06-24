package pet_finder.validations;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.models.Comentario;
import pet_finder.repositories.ComentarioRepository;

@Component
public class ComentarioValidation {
    private final ComentarioRepository repository;

    public ComentarioValidation(ComentarioRepository repository) {
        this.repository = repository;
    }

    public Comentario existePorId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro un comentario con esa id"));
    }

    public void esActivo(Boolean activo) {
        if (Boolean.FALSE.equals(activo)) {
            throw new IllegalStateException("El comentario ya fue dado de baja.");
        }
    }
}
