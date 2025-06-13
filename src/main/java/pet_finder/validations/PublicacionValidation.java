package pet_finder.validations;

import org.springframework.stereotype.Component;
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
}
