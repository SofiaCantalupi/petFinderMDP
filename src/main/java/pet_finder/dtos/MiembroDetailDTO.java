package pet_finder.dtos;

import pet_finder.models.Miembro;

public record MiembroDetailDTO(Long id, String nombre, String apellido, String email, String rol, boolean activo) {

    public MiembroDetailDTO(Miembro miembro) {
        this(
                miembro.getId(),
                miembro.getNombre(),
                miembro.getApellido(),
                miembro.getEmail(),
                miembro.getRol().name(),
                miembro.isActivo()
        );
    }

}
