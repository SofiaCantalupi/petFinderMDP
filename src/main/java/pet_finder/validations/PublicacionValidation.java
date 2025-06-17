package pet_finder.validations;

import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.exceptions.MiembroInactivoException;
import pet_finder.models.Publicacion;

/**
 * @author Daniel Herrera
 */
@Component
public class PublicacionValidation {

    public PublicacionValidation () {}

    public void esInactivo(Publicacion publicacion){
        if (!publicacion.getActivo()){
            throw new MiembroInactivoException("La Publicacion con ID : "+publicacion.getId()+" esta inactiva.");
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
