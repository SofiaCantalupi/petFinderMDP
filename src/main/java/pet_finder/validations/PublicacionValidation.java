package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Publicacion;
import pet_finder.repositories.PublicacionRepository;


@Component
public class PublicacionValidation {
    public final PublicacionRepository repository;

    public PublicacionValidation (PublicacionRepository repository) {
        this.repository = repository;
    }

    public void esActivo(Boolean activo){
        if (Boolean.FALSE.equals(activo)){
            throw new IllegalArgumentException("La publicacion se encuentra inactiva.");
        }
    }

    //Valida que la mascota que se quiera asignar no este asociada a otra publicación.
    public void mascotaYaAsignada(Long mascotaId){
        if(repository.existsByMascotaId(mascotaId)){
            throw new IllegalArgumentException("La mascota se encuentra asociada a otra publicación.");
        }
    }

    // Si no existe lanza una excepcion, de caso contrario retorna la entidad
    public Publicacion existePorId(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe una publicación con el ID ingresado."));
    }

}
