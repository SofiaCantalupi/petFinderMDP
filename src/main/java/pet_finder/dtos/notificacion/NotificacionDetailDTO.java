package pet_finder.dtos.notificacion;

import pet_finder.enums.TipoNotificacion;
import pet_finder.models.Notificacion;

import java.time.LocalDate;

public record NotificacionDetailDTO(
        Long id,
        Long receptorId,
        Long emisorId,
        TipoNotificacion tipo,
        boolean leida,
        LocalDate fecha,
        boolean activa
) {

    public NotificacionDetailDTO(Notificacion notificacion) {
        this(
                notificacion.getId(),
                notificacion.getReceptor().getId(),
                notificacion.getEmisor().getId(),
                notificacion.getTipo(),
                notificacion.isLeida(),
                notificacion.getFecha(),
                notificacion.isActiva()
        );
    }
}

