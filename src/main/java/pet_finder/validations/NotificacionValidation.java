package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.exceptions.OperacionNoPermitidaException;
import pet_finder.models.Notificacion;
import pet_finder.repositories.NotificacionRepository;

@Component
public class NotificacionValidation {

    private final NotificacionRepository notificacionRepository;

    public NotificacionValidation(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    public Notificacion existePorId(Long id){
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro una notificacion con ese id"));
    }

    public void esActivo(Notificacion notificacion){
        if(!notificacion.isActiva()){
            throw new IllegalArgumentException("La notificación se encuentra inactiva.");
        }
    }

    public void validarEmisorYReceptor(Long receptorId,Long emisorId){

        if (receptorId.equals(emisorId)) {
            throw new OperacionNoPermitidaException(
                    "Un miembro no puede generarse una notificación a sí mismo.");
        }
    }

}
