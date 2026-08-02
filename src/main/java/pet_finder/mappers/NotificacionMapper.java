package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.notificacion.NotificacionDetailDTO;
import pet_finder.models.Notificacion;

import java.util.List;

@Component
public class NotificacionMapper {

    public NotificacionDetailDTO aDetail(Notificacion notificacion){
        return new NotificacionDetailDTO(notificacion);
    }

    public List<NotificacionDetailDTO> deEntidadesAdetails(List<Notificacion> notificaciones){
        return notificaciones.stream()
                .map(this::aDetail)
                .toList();
    }
}