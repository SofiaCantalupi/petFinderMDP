package pet_finder.dtos;

/**
 * @author Daniel Herrera
 */

public record UbicacionDetailDTO(
        String direccion,
        Integer altura,
        String ciudad,
        String region, // Estado, provincia o departamento
        String pais,
        Boolean activo
) {
}
