package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.models.SolicitudAdopcion;

import java.util.List;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {
    boolean existsByPublicacion_IdAndMiembroSolicitante_IdAndEstadoIn(Long idPublicacion, Long idMiembroSolicitante, List<EstadoSolicitud> estados);

    boolean existsByPublicacion_IdAndMiembroSolicitante_IdAndEstadoAndMotivoRechazo(
            Long idPublicacion, Long idMiembroSolicitante, EstadoSolicitud estado, MotivoRechazo motivoRechazo);

    List<SolicitudAdopcion> findByPublicacion_Miembro_IdAndPublicacion_ActivoTrue(Long idMiembroDuenio);

    List<SolicitudAdopcion> findByMiembroSolicitante_IdAndPublicacion_ActivoTrue(Long idMiembroSolicitante);

    List<SolicitudAdopcion> findByPublicacion_IdAndEstado(Long idPublicacion, EstadoSolicitud estado);
}
