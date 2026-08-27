package pet_finder.dtos.mensaje;

import java.time.LocalDateTime;

public record ConversacionDetailDTO(
        Long idMiembro,
        String nombre,
        String apellido,
        boolean activo, // usado para que el front pueda mostrar "usuario eliminado"
        Long mensajesNoLeidos,
        String ultimoMensaje,
        LocalDateTime fechaUltimoMensaje
) {
}
