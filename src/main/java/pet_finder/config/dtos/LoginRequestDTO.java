package pet_finder.config.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "El email es obligatorio") @Email String email,
        @NotBlank(message = "La contraseña es obligatoria") String contrasenia
) {}

