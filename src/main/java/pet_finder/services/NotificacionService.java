package pet_finder.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.enums.TipoNotificacion;
import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.models.Notificacion;
import pet_finder.models.SolicitudAdopcion;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.NotificacionRepository;
import pet_finder.repositories.SolicitudAdopcionRepository;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.NotificacionValidation;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final MiembroValidation miembroValidation;
    private final NotificacionValidation notificacionValidation;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final MiembroRepository miembroRepository; //!!! usar validation

    public NotificacionService(NotificacionRepository notificacionRepository, MiembroValidation miembroValidation, NotificacionValidation notificacionValidation, SolicitudAdopcionRepository solicitudAdopcionRepository, MiembroRepository miembroRepository) {
        this.notificacionRepository = notificacionRepository;
        this.miembroValidation = miembroValidation;
        this.notificacionValidation = notificacionValidation;
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.miembroRepository = miembroRepository;
    }

    public Notificacion generarNotificacion(Long receptorId, Long emisorId, TipoNotificacion tipo, Long entidadReferenciaId){

        //Valido que no tengan los mismos ID, que significaria que son el mismo miembro
        notificacionValidation.validarEmisorYReceptor(receptorId, emisorId);

        //Obtengo los objetos.
        Miembro receptor = miembroRepository.findById(receptorId).orElseThrow(() -> new UsuarioNoEncontradoException(
                "No se encontró un miembro activo con ese ID."));
        Miembro emisor = miembroRepository.findById(emisorId).orElseThrow(() -> new UsuarioNoEncontradoException(
                "No se encontró un miembro activo con ese ID."));

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
        miembroRepository.findByIdAndActivoTrue(receptorId).orElseThrow(() -> new UsuarioNoEncontradoException(
                "No se encontró un miembro activo con ese ID."));

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
        miembroRepository.findByIdAndActivoTrue(receptorId).orElseThrow(() -> new UsuarioNoEncontradoException(
                "No se encontró un miembro activo con ese ID."));

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
        miembroRepository.findByIdAndActivoTrue(receptorId).orElseThrow(() -> new UsuarioNoEncontradoException(
                "No se encontró un miembro activo con ese ID."));

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
                        .findByTipoAndEntidadReferenciaIdAndActivaTrue(
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

    //Uso exclusivo de MiembroService al dar de baja una cuenta (propia o por un administrador):
    //da de baja las notificaciones en las que participa el miembro, tanto las que emitio
    //como las que tenia en su bandeja.
    @Transactional
    public void eliminarNotificacionesPorMiembro(Long idMiembro){

        //Las dos listas nunca se solapan: validarEmisorYReceptor garantiza que emisor != receptor.
        List<Notificacion> notificaciones = new ArrayList<>();
        notificaciones.addAll(notificacionRepository.findByEmisorIdAndActivaTrue(idMiembro));
        notificaciones.addAll(notificacionRepository.findByReceptorIdAndActivaTrue(idMiembro));

        notificaciones.forEach(n -> n.setActiva(false));

        notificacionRepository.saveAll(notificaciones);
    }

    // Notifica a todos los solicitantes que fueron rechazados automáticamente
    // cuando una mascota vuelve a estar disponible para adopción.
@Transactional
public void notificarSolicitantesPorAdopcionDisponible(Long publicacionId) {

    List<SolicitudAdopcion> solicitudes =
            solicitudAdopcionRepository.findByPublicacion_IdAndEstadoAndMotivoRechazoIn(
                    publicacionId,
                    EstadoSolicitud.RECHAZADA,
                    List.of(
                            MotivoRechazo.AUTO_POR_OTRA_APROBADA,
                            MotivoRechazo.AUTO_CAMBIO_ESTADO_MASCOTA
                    ));

    for (SolicitudAdopcion solicitud : solicitudes) {

        boolean yaNotificado = notificacionRepository.existsByTipoAndEntidadReferenciaId(
                TipoNotificacion.ADOPCION_DISPONIBLE_NUEVAMENTE,
                solicitud.getId());

        if (!yaNotificado) {
            generarNotificacion(
                    solicitud.getMiembroSolicitante().getId(),
                    solicitud.getPublicacion().getMiembro().getId(),
                    TipoNotificacion.ADOPCION_DISPONIBLE_NUEVAMENTE,
                    solicitud.getId());
        }
    }
}

}


