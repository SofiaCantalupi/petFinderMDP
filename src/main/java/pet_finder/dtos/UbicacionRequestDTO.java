package pet_finder.dtos;

import jakarta.validation.constraints.NotBlank;

public record UbicacionRequestDTO (
        @NotBlank(message = "La ciudad no puede estar vacia.")
        String ciudad,
        @NotBlank(message = "La region no puede estar vacia.")
        String region, // Estado, provincia o departamento
        @NotBlank(message = "El pais no puede estar vacio.")
        String pais
) {
}
