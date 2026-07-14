package pet_finder.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import pet_finder.dtos.solicitud.ResolucionSolicitudRequestDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.exceptions.OperacionNoPermitidaException;
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

    public SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository, PublicacionService publicacionService, MiembroService miembroService, SolicitudAdopcionValidation solicitudValidation, SolicitudAdopcionMapper solicitudMapper) {
        this.solicitudRepository = solicitudRepository;
        this.publicacionService = publicacionService;
        this.miembroService = miembroService;
        this.solicitudValidation = solicitudValidation;
        this.solicitudMapper = solicitudMapper;
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
        return solicitudMapper.aDetail(guardada);
    }

    @Transactional(readOnly = true)
    public List<SolicitudAdopcionDetailDTO> listarRecibidas(Long idMiembroDuenio) {
        List<SolicitudAdopcion> solicitudes = solicitudRepository.findByPublicacion_Miembro_IdAndPublicacion_ActivoTrue(idMiembroDuenio);

        return solicitudMapper.deEntidadesAdetails(solicitudes);
    }

    @Transactional(readOnly = true)
    public List<SolicitudAdopcionDetailDTO> listarEnviadas(Long idMiembroSolicitante) {
        List<SolicitudAdopcion> solicitudes = solicitudRepository.findByMiembroSolicitante_IdAndPublicacion_ActivoTrue(idMiembroSolicitante);

        return solicitudMapper.deEntidadesAdetails(solicitudes);
    }

    @Transactional
    public SolicitudAdopcionDetailDTO resolverSolicitud(Long idSolicitud, Long idMiembroLoggeado, ResolucionSolicitudRequestDTO resolucionRequest) {
        SolicitudAdopcion solicitud = solicitudValidation.existePorId(idSolicitud);

        if (!solicitud.getPublicacion().getMiembro().getId().equals(idMiembroLoggeado)) {
            throw new OperacionNoPermitidaException("Solo el dueño de la publicación puede resolver la solicitud de adopción.");
        }

        if (!solicitud.getEstado().equals(EstadoSolicitud.PENDIENTE)){
            throw new IllegalArgumentException("Esta solicitud ya fue resuelta.");
        }

        EstadoSolicitud nuevoEstado = solicitudValidation.validarYConvertirResolucion(resolucionRequest.getEstado());

        solicitud.setEstado(nuevoEstado);
        solicitud.setFechaResolucion(LocalDateTime.now());

        if (nuevoEstado == EstadoSolicitud.RECHAZADA) {
            solicitud.setMotivoRechazo(MotivoRechazo.MANUAL);

            if (resolucionRequest.getComentarioResolucion() != null && !resolucionRequest.getComentarioResolucion().isBlank()) {
                solicitud.setComentarioResolucion(resolucionRequest.getComentarioResolucion());
            }
        }

        // AGREGAR METODO QUE CAMBIA EL ESTADO DE LA MASCOTA A ADOPTADO Y QUE CAMBIA EL ESTADO DE LAS OTRAS SOLICITUDES PENDIENTES A RECHAZADAS AUTO

        SolicitudAdopcion guardada = solicitudRepository.save(solicitud);

        return solicitudMapper.aDetail(guardada);
    }
}
