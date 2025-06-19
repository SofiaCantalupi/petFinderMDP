package pet_finder.validations;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.models.Comentario;
import pet_finder.repositories.ComentarioRepositories;

@Component
public class ComentarioValidation {
    private final ComentarioRepositories comentarioRepositories;

    public ComentarioValidation(ComentarioRepositories comentarioRepositories) {
        this.comentarioRepositories = comentarioRepositories;
    }

    public Comentario existePorId(Long id){
        return comentarioRepositories.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro un comentario con esa id"));
    }

    public void esActivo(Boolean activo) {
        if (Boolean.FALSE.equals(activo)) {
            throw new IllegalStateException("El comentario ya fue dado de baja.");
        }
    }
}
