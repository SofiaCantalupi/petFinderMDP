package pet_finder.dtos.mensaje;

public record ConversacionDetailDTO(
        Long idMiembro,
        String nombre,
        String apellido,
        Long mensajesNoLeidos
) {
}
