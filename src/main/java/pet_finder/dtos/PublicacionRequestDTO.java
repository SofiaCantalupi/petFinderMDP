package pet_finder.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PublicacionRequestDTO (
        @NotBlank(message = "Ingrese alguna información descriptiva de la mascota.")
        String descripcion,
        @NotNull(message="Debe indicar la mascota")
        Long mascotaId,
        @NotNull(message="Debe indicar al miembro relacionado")
        Long miembroId,
        //@NotNull(message="Debe indicar la ubicacion")
        //Long ubicacionId,
        @Valid
        UbicacionRequestDTO ubicacion
) {}
