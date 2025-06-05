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

        // todo: El nombre de la mascota puede ser nulo?
        // todo: Alguien rescata al animal, pero no sabe su nombre y no tiene collar..
        String nombreMascota,
        String tipoMascota,
        // todo : CAMBIAR AL REQUEST MASCOTA DTO
        // @Valid
        // MascotaRequestDTO mascotaDTO

        Long miembroId,
        String nombreMiembro,

        String direccion,
        Integer altura,
        String ciudad,
        String region,
        String pais
) {}