package pet_finder.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pet_finder.dtos.solicitud.ResolucionSolicitudRequestDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.enums.TipoNotificacion;
import pet_finder.mappers.SolicitudAdopcionMapper;
import pet_finder.models.Publicacion;
import pet_finder.models.SolicitudAdopcion;
import pet_finder.repositories.SolicitudAdopcionRepository;
import pet_finder.validations.SolicitudAdopcionValidation;
import pet_finder.models.Miembro;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudAdopcionService {
    private final SolicitudAdopcionRepository solicitudRepository;
    private final PublicacionService publicacionService;
    private final MiembroService miembroService;
    private final SolicitudAdopcionValidation solicitudValidation;
    private final SolicitudAdopcionMapper solicitudMapper;
    private final NotificacionService notificacionService;

    public SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository,
            PublicacionService publicacionService, MiembroService miembroService,
            SolicitudAdopcionValidation solicitudValidation, SolicitudAdopcionMapper solicitudMapper,
            NotificacionService notificacionService) {
        this.solicitudRepository = solicitudRepository;
        this.publicacionService = publicacionService;
        this.miembroService = miembroService;
        this.solicitudValidation = solicitudValidation;
        this.solicitudMapper = solicitudMapper;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public SolicitudAdopcionDetailDTO guardar(SolicitudAdopcionRequestDTO request, Long idMiembroSolicitante) {
        // Se obtiene la publicacion completa
        Publicacion publicacion = publicacionService.obtenerPorId(request.getIdPublicacion());
        // Se obtiene el ID del miembro que hizo la publicacion
        Long idMiembroDuenio = publicacion.getMiembro().getId();

        // Se obtiene el Miembro que realiza la solicitud
        Miembro miembroSolicitante = miembroService.obtenerPorId(idMiembroSolicitante);

        solicitudValidation.validarEstadoMascotaParaAdopcion(publicacion.getMascota().getEstadoMascota());
        solicitudValidation.validarQueNoSeaDuenioPublicacion(idMiembroSolicitante, idMiembroDuenio);
        solicitudValidation.validarSinSolicitudBloqueante(publicacion.getId(), idMiembroSolicitante);

        SolicitudAdopcion solicitud = solicitudMapper.aEntidad(request);
        solicitud.setPublicacion(publicacion);
        solicitud.setMiembroSolicitante(miembroSolicitante);

        SolicitudAdopcion guardada = solicitudRepository.save(solicitud);

        // Genero notificacion al dueño de la publicacion y guardo el ID de la solicitud
        // de adopcion
        notificacionService.generarNotificacion(
                idMiembroDuenio,
                idMiembroSolicitante,
                TipoNotificacion.SOLICITUD_ADOPCION,
                guardada.getId());

        return solicitudMapper.aDetail(guardada);
    }

    @Transactional(readOnly = true)
    public List<SolicitudAdopcionDetailDTO> listarRecibidas(Long idMiembroDuenio, String estadoParam) {
        List<SolicitudAdopcion> solicitudes = solicitudRepository
                .findByPublicacion_Miembro_IdAndPublicacion_ActivoTrue(idMiembroDuenio);

        if (estadoParam != null && !estadoParam.isBlank()) {
            EstadoSolicitud estadoSolicitud = solicitudValidation.validarYConvertirEstadoSolicitud(estadoParam);
            solicitudes = solicitudes.stream().filter(s -> s.getEstado() == estadoSolicitud).toList();
        }

        return solicitudMapper.deEntidadesAdetails(solicitudes);
    }

    @Transactional(readOnly = true)
    public List<SolicitudAdopcionDetailDTO> listarEnviadas(Long idMiembroSolicitante, String estadoParam) {
        List<SolicitudAdopcion> solicitudes = solicitudRepository
                .findByMiembroSolicitante_IdAndPublicacion_ActivoTrue(idMiembroSolicitante);

        if (estadoParam != null && !estadoParam.isBlank()) {
            EstadoSolicitud estadoSolicitud = solicitudValidation.validarYConvertirEstadoSolicitud(estadoParam);
            solicitudes = solicitudes.stream().filter(s -> s.getEstado() == estadoSolicitud).toList();
        }

        return solicitudMapper.deEntidadesAdetails(solicitudes);
    }

    @Transactional
    public void revertirPendientes(Long idPublicacion, MotivoRechazo motivoRechazo) {
        List<SolicitudAdopcion> solicitudes = solicitudRepository.findByPublicacion_IdAndEstado(idPublicacion,
                EstadoSolicitud.PENDIENTE);

        solicitudes.forEach(solicitud -> {

            solicitud.setEstado(EstadoSolicitud.RECHAZADA);
            solicitud.setMotivoRechazo(MotivoRechazo.AUTO_POR_OTRA_APROBADA);
            solicitud.setFechaResolucion(LocalDateTime.now());

            notificacionService.generarNotificacion(
                    solicitud.getMiembroSolicitante().getId(),
                    solicitud.getPublicacion().getMiembro().getId(),
                    TipoNotificacion.RESPUESTA_ADOPCION,
                    solicitud.getId());
        });
    }

    @Transactional
    public SolicitudAdopcionDetailDTO resolverSolicitudAdopcion(Long idSolicitud, Long idMiembroLoggeado,
            ResolucionSolicitudRequestDTO resolucionRequest) {

        SolicitudAdopcion solicitud = solicitudValidation.existePorId(idSolicitud);

        // Se valida que la publicacion de la solicitud sea del mismo miembro que el que
        // la resuelve
        solicitudValidation.validarDuenioPublicacion(solicitud, idMiembroLoggeado);

        // Se valida que el estado sea distinto de pendiente, es decir, que no este
        // resuelta aun
        solicitudValidation.validarPendiente(solicitud);

        // Se valida que la mascota sigua "en_adopcion"
        solicitudValidation.validarMascotaEnAdopcion(solicitud);

        // Se obtiene el Enum que representa al nuevo Estado de la resolucion
        EstadoSolicitud nuevoEstado = solicitudValidation.validarYConvertirResolucion(resolucionRequest.getEstado());

        // Se settea el nuevo estado y fecha a la Solicitud
        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaResolucion(LocalDateTime.now());

        // Si el nuevo estado es rechazada, se le settea el motivo como "manual"
        // Si el nuevo estado es rechazada y contiene un comentario, se le settea esta
        // ultimo
        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            solicitud.setMotivoRechazo(MotivoRechazo.MANUAL);
        }

        // Si la resolucion tiene un comentario, se guarda
        if (resolucionRequest.getComentarioResolucion() != null
                && !resolucionRequest.getComentarioResolucion().isBlank()) {
            solicitud.setComentarioResolucion(resolucionRequest.getComentarioResolucion());
        }

        // Se cambia el estado de la mascota de "en adopcion" a "adoptado" si se acepta
        // la solicitud. Ademas se revierten las pendientes a "rechazadas" por motivo
        // auto
        if (nuevoEstado.equals(EstadoSolicitud.APROBADA)) {
            publicacionService.marcarComoAdoptada(solicitud.getPublicacion().getId(), idMiembroLoggeado);
            revertirPendientes(solicitud.getPublicacion().getId(), MotivoRechazo.AUTO_POR_OTRA_APROBADA);
        }

        SolicitudAdopcion guardada = solicitudRepository.save(solicitud);

        // Se manda una notificacion a cada miembro Solicitante sobre la respuesta de
        // adopcion.
        notificacionService.generarNotificacion(
                solicitud.getMiembroSolicitante().getId(),
                solicitud.getPublicacion().getMiembro().getId(),
                TipoNotificacion.RESPUESTA_ADOPCION,
                guardada.getId());

        return solicitudMapper.aDetail(guardada);
    }

    @Transactional
    public SolicitudAdopcionDetailDTO cancelarSolicitudPropia(Long idMiembro, Long idSolicitud) {

        SolicitudAdopcion solicitud = solicitudValidation.validarQueSolicitudSeaPropia(idMiembro, idSolicitud);

        // Solo se puede cancelar mientras el dueño todavía no la resolvió.
        solicitudValidation.validarPendiente(solicitud);

        solicitud.setEstado(EstadoSolicitud.CANCELADA);

        // Elimino las notificaciones asociadas al ID de esta solicitud de adopcion
        notificacionService.eliminarNotificacionesSolicitud(solicitud.getId());

        return solicitudMapper.aDetail(solicitud);
    }
}
