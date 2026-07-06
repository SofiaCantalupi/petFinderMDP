package pet_finder.dtos.solicitud;
import pet_finder.models.SolicitudAdopcion;
import java.time.LocalDateTime;

public record SolicitudAdopcionDetailDTO(
        Long id,
        String estado,
        LocalDateTime fecha,

        Long idPublicacion,
        String nombreMascota,
        String estadoMascota,
        String tipoMascota,

        String celular,
        Long idMiembroSolicitante,
        String nombreCompletoSolicitante,

        String tipoHogar,
        boolean hayMascotaEnHogar,
        String tipoMascotaEnHogar,
        boolean tienePatio,
        boolean aceptaCondiciones,
        String motivoAdopcion,

        LocalDateTime fechaResolucion,
        String comentarioResolucion
) {
    public SolicitudAdopcionDetailDTO(SolicitudAdopcion solicitud) {
        this(
                solicitud.getId(),
                solicitud.getEstado().getValorFront(),
                solicitud.getFecha(),

                solicitud.getPublicacion().getId(),
                solicitud.getPublicacion().getMascota().getNombre(),
                solicitud.getPublicacion().getMascota().getEstadoMascota().getValorFront(),
                solicitud.getPublicacion().getMascota().getTipoMascota().getValorFront(),

                solicitud.getCelular(),
                solicitud.getMiembroSolicitante().getId(),
                solicitud.getMiembroSolicitante().getNombre() + " " + solicitud.getMiembroSolicitante().getApellido(),

                solicitud.getTipoHogar().getValorFront(),
                solicitud.isHayMascotaEnHogar(),
                solicitud.getTipoMascotasEnHogar().getValorFront() != null ? solicitud.getTipoMascotasEnHogar().getValorFront() : null,
                solicitud.isTienePatio(),
                solicitud.isAceptaCondiciones(),
                solicitud.getMotivoAdopcion(),

                solicitud.getFechaResolucion(),
                solicitud.getComentarioResolucion()
        );
    }
}
