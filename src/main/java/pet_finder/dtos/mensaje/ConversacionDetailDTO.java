package pet_finder.dtos.mensaje;

import java.time.LocalDateTime;

public record ConversacionDetailDTO(
        Long idMiembro,
        String nombre,
        String apellido,
        Long mensajesNoLeidos,
        String ultimoMensaje,
        LocalDateTime fechaUltimoMensaje
) {
}
