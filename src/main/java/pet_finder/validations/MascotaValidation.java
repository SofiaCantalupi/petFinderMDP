package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.models.Mascota;
import pet_finder.repositories.MascotaRepository;

@Component
public class MascotaValidation {
    private final MascotaRepository repository;

    public MascotaValidation(MascotaRepository repository) {
        this.repository = repository;
    }

    // Si no existe lanza una excepcion, de caso contrario retorna la entidad
    public Mascota existePorId(Long id){
        return repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No se encontró una mascota con el ID: " + id));
    }

    public void esActivo(Boolean activo) {
        if (Boolean.FALSE.equals(activo)) {
            throw new IllegalStateException("No se puede modificar o eliminar una mascota que fue dada de baja.");
        }
    }
}
