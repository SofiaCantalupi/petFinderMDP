package pet_finder.dtos;

public record UbicacionDetailDTO(
        String ciudad,
        String region, // Estado, provincia o departamento
        String pais,
        Boolean activo
) {
}
