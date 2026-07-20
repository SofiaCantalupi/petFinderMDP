package pet_finder.validations;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.models.SolicitudAdopcion;
import pet_finder.repositories.SolicitudAdopcionRepository;

import java.util.List;

@Component
public class SolicitudAdopcionValidation {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    public SolicitudAdopcionValidation(SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
    }

    public SolicitudAdopcion existePorId(Long idSolicitud){
        return solicitudAdopcionRepository.findById(idSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró una solucitud de adopción con ese ID."));
    }

    public void validarEstadoMascotaParaAdopcion(EstadoMascota estadoMascota) {
        if (!estadoMascota.equals(EstadoMascota.EN_ADOPCION)) {
            throw new IllegalArgumentException("La mascota de la publicación no está disponible para adopción.");
        }
    }

    public void validarQueNoSeaDuenioPublicacion(Long idMiembroSolicitante, Long idMiembroPublicacion) {
        if (idMiembroPublicacion.equals(idMiembroSolicitante)) {
            throw new IllegalArgumentException("No podés solicitar la adopción de tu propia mascota publicada.");
        }
    }

    /* Metodo que valida si existen solicitudes de adopcion pendientes, aceptadas o rechazadas (por el duenio de la publicacion), relacionada a la publicacion a la que se esta solicitando
    * Solo se puede hacer una solicitud por publicacion*/
    public void validarSinSolicitudBloqueante(Long idPublicacion, Long idMiembroSolicitante) {
        // Busca solicitudes del miembro que hace la solicitud que este pendiente o aprobada, y pertenezca a la misma publicacion
        boolean existeSolicitudPendienteOAprobada = solicitudAdopcionRepository.existsByPublicacion_IdAndMiembroSolicitante_IdAndEstadoIn(idPublicacion, idMiembroSolicitante, List.of(EstadoSolicitud.PENDIENTE, EstadoSolicitud.APROBADA));
        // Busca solicitudes del miembro que hayan sido rechazadas por el duenio de la publicacion
        boolean tieneRechazoManual = solicitudAdopcionRepository.existsByPublicacion_IdAndMiembroSolicitante_IdAndEstadoAndMotivoRechazo(idPublicacion, idMiembroSolicitante, EstadoSolicitud.RECHAZADA, MotivoRechazo.MANUAL);

        if(existeSolicitudPendienteOAprobada || tieneRechazoManual){
            throw new IllegalArgumentException("Ya tenés una solicitud de adopción asociada a esta publicación.");
        }
    }

    public EstadoSolicitud validarYConvertirResolucion(String estadoResolucion){
        EstadoSolicitud estado;

        try {
            estado = EstadoSolicitud.valueOf(estadoResolucion.toUpperCase());
        }catch (IllegalArgumentException exc){
            throw new IllegalArgumentException("Estado de resolucion invalido.");
        }

        if(estado != EstadoSolicitud.APROBADA && estado != EstadoSolicitud.RECHAZADA){
            throw new IllegalArgumentException("Una solucitud de adopción sólo puede ser aceptada o rechazada.");
        }

        return estado;
    }
}
