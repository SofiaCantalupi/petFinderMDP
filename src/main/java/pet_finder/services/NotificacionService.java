package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.enums.TipoNotificacion;
import pet_finder.models.Miembro;
import pet_finder.models.Notificacion;
import pet_finder.repositories.NotificacionRepository;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.NotificacionValidation;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final MiembroService miembroService;
    private final MiembroValidation miembroValidation;
    private final NotificacionValidation notificacionValidation;

    public NotificacionService(NotificacionRepository notificacionRepository, MiembroService miembroService, MiembroValidation miembroValidation, NotificacionValidation notificacionValidation) {
        this.notificacionRepository = notificacionRepository;
        this.miembroService = miembroService;
        this.miembroValidation = miembroValidation;
        this.notificacionValidation = notificacionValidation;
    }

    public Notificacion generarNotificacion(Long receptorId, Long emisorId, TipoNotificacion tipo, Long entidadReferenciaId){

        //Valido que no tengan los mismos ID, que significaria que son el mismo miembro
        notificacionValidation.validarEmisorYReceptor(receptorId, emisorId);

        //Obtengo los objetos.
        Miembro receptor = miembroService.obtenerPorId(receptorId);
        Miembro emisor = miembroService.obtenerPorId(emisorId);

        //Creo la notificacion
        Notificacion notificacion = new Notificacion(receptor,emisor,tipo,entidadReferenciaId);

        return notificacionRepository.save(notificacion);
    }

    public Notificacion obtenerPorId(Long id){

        //Verifico si existe la notificacion por el ID
        Notificacion existente = notificacionValidation.existePorId(id);
        //Valido que este activa.
        notificacionValidation.esActivo(existente);

        return existente;
    }

    // Listar las notificaciones del miembro autenticado
    public List<Notificacion> listarPropias(Long receptorId){

        // Valida que el miembro exista y esté activo
        miembroService.obtenerPorId(receptorId);

        return notificacionRepository.findByReceptorIdAndActivaTrue(receptorId);
    }

    public Notificacion marcarComoLeida(Long notificacionId, Long miembroLogueadoId){

        Notificacion notificacion = obtenerPorId(notificacionId);

        // Valida que la notificación pertenezca al usuario autenticado
        //Es necesario porque pasamos el ID de la notificacion. Distinto al metodo MarcarTodasComoLeidas.
        miembroValidation.estaLogeado(
                notificacion.getReceptor().getId(),
                miembroLogueadoId);

        // Si ya está leída no hacemos nada
        if (!notificacion.isLeida()) {
            notificacion.setLeida(true);
        }

        return notificacionRepository.save(notificacion);
    }

    public void marcarTodasComoLeidas(Long receptorId){

        // Valida que el miembro exista y esté activo
        miembroService.obtenerPorId(receptorId);

        List<Notificacion> notificaciones =
                notificacionRepository.findByReceptorIdAndActivaTrue(receptorId);

        for (Notificacion notificacion : notificaciones) {
            if (!notificacion.isLeida()) {
                notificacion.setLeida(true);
            }
        }

        notificacionRepository.saveAll(notificaciones);
    }

    public long contarNoLeidas(Long receptorId){

        // Valida que el miembro exista y esté activo
        miembroService.obtenerPorId(receptorId);

        return notificacionRepository.countByReceptorIdAndActivaTrueAndLeidaFalse(receptorId);
    }

    public void eliminarNotificacion(Long notificacionId, Long miembroLogueadoId){

        // Obtiene la notificación y valida que exista y esté activa
        Notificacion notificacion = obtenerPorId(notificacionId);

        // Valida que el usuario autenticado sea el dueño de la notificación
        miembroValidation.estaLogeado(
                notificacion.getReceptor().getId(),
                miembroLogueadoId);

        notificacion.setActiva(false);

        notificacionRepository.save(notificacion);
    }

    //Metodo para reutilizar en el eliminado de varias notificaciones asociadas a un comentario o una solicitud de adopcion.
    private void eliminarPorReferencia(TipoNotificacion tipo, Long referenciaId){

        List<Notificacion> notificaciones =
                notificacionRepository
                        .findByTipoAndReferenciaIdAndActivaTrue(
                                tipo,
                                referenciaId);

        notificaciones.forEach(n -> n.setActiva(false));

        notificacionRepository.saveAll(notificaciones);
    }

    public void eliminarNotificacionesComentario(Long comentarioId){
        eliminarPorReferencia(TipoNotificacion.NUEVO_COMENTARIO, comentarioId);
    }

    public void eliminarNotificacionesSolicitud(Long solicitudId){
        eliminarPorReferencia(
                TipoNotificacion.SOLICITUD_ADOPCION,
                solicitudId);
    }

}


