package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.exceptions.MiembroInactivoException;
import pet_finder.models.Publicacion;
import pet_finder.repositories.PublicacionRepository;

/**
 * @author Daniel Herrera
 */
@Component
public class PublicacionValidation {
    public final PublicacionRepository repository;

    public PublicacionValidation (PublicacionRepository repository) {
        this.repository = repository;
    }

    public void esActivo(Publicacion publicacion){
        if (!publicacion.getActivo()){
            throw new IllegalArgumentException("La Publicacion con ID : "+publicacion.getId()+" esta inactiva.");
        }
    }

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

    // Es utilizado para transformar el string que representa el tipo de mascota a su respectivo Enum.
    // Si el valor (tipo) no es valido, se lanza una excepcion
    public TipoMascota validarYConvertirTipoMascota(String tipoString){
        try{
            // Se trata de convertir el string a su equivalente Enum
            return TipoMascota.valueOf(tipoString.toUpperCase());
        }catch (IllegalArgumentException exc){
            throw new IllegalArgumentException("Tipo de mascota inválido.");
        }
    }

    // Es utilizado para transformar el string que representa el estado de una mascota a su respectivo Enum.
    public EstadoMascota validarYConvertirEstadoMascota(String tipoString){
        try {
            return EstadoMascota.valueOf(tipoString.toUpperCase());
        }catch (IllegalArgumentException exc){
            throw new IllegalArgumentException("Estado de mascota inválido.");
        }
    }
}
