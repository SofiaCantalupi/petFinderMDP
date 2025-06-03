package pet_finder.dtos;

import java.time.LocalDate;

public record PublicacionDetailDTO (
        Long id,
        String descripcion,
        LocalDate fecha,
        Boolean activo,
        Long mascotaId,
        String nombreMascota,
        Long miembroId,
        String nombreMiembro,
        String ciudad,
        String region,
        String pais
) {}