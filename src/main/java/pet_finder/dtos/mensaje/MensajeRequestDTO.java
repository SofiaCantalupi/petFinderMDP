package pet_finder.dtos.mensaje;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MensajeRequestDTO {

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String texto;

    @NotNull(message = "Debe indicar el receptor del mensaje")
    private Long idReceptor;

    public MensajeRequestDTO() {
    }

    public String getTexto() {
        return texto;
    }

    public Long getIdReceptor() {
        return idReceptor;
    }
}
