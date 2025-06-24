package pet_finder.dtos.norma;

import jakarta.validation.constraints.NotBlank;

public class NormaComunidadRequestDTO {

    @NotBlank(message = "Debe ingresar una norma.")
    private final String texto;

    public NormaComunidadRequestDTO(String texto) {
        this.texto = texto;
    }

    // Getter
    public @NotBlank(message = "Debe ingresar una norma.") String getTexto() {
        return texto;
    }
}
