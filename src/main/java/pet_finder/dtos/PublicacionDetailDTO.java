package pet_finder.dtos;

import java.time.LocalDate;

/**
 * @author Daniel Herrera
 */

public record PublicacionDetailDTO (
        Long id,
        String descripcion,
        LocalDate fecha,
        Boolean activo,

        MascotaDetailDTO mascotaDTO,

        // todo: Considerar cambiar a miembroDetailDTO
        Long miembroId,
        String miembroEmail,

        // todo: Considerar cambiar a ubicacionDetailDTO
        String direccion,
        Integer altura,
        String ciudad,
        String region,
        String pais
) {}