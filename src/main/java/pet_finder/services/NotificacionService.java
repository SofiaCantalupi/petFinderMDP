package pet_finder.services;

import pet_finder.models.Notificacion;
import pet_finder.repositories.NotificacionRepository;

public class NotificacionService {

    //- CrearNotificacion( )
    //- ListarNotificaciones ( )
    //- MarcarComoLeida ( )
    //- MarcarTodasComoLeida ( )
    //- ContarNoLeidas ( )
    //- BorrarNotificacion ( )

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }
}
