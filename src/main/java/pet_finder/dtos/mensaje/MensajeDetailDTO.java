package pet_finder.dtos.mensaje;

import pet_finder.models.Mensaje;

import java.time.LocalDateTime;

public record MensajeDetailDTO(
        Long id,
        String texto,
        LocalDateTime fechaEnvio,
        Boolean leido,
        Long idEmisor,
        String nombreEmisor,
        Long idReceptor,
        String nombreReceptor
) {
    public MensajeDetailDTO(Mensaje mensaje) {
        this(
                mensaje.getId(),
                mensaje.getTexto(),
                mensaje.getFechaEnvio(),
                mensaje.getLeido(),
                mensaje.getEmisor().getId(),
                mensaje.getEmisor().getNombre(),
                mensaje.getReceptor().getId(),
                mensaje.getReceptor().getNombre()
        );
    }
}
