package pet_finder.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Daniel Herrera
 */

public record UbicacionRequestDTO (
        @NotBlank(message = "La direccion no puede estar vacia.")
        @Size(max = 100, message = "La dirección no puede tener más de 100 caracteres.")
        String direccion,
        @Min(value = 0, message = "La altura no puede ser negativa.")
        Integer altura,
        @NotBlank(message = "La ciudad no puede estar vacia.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        String ciudad,
        @NotBlank(message = "La region no puede estar vacia.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        String region, // Estado, provincia o departamento
        @NotBlank(message = "El pais no puede estar vacio.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        String pais
) {
}
