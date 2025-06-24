package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
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
            throw new IllegalStateException("La mascota ya fue dada de baja.");
        }
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
