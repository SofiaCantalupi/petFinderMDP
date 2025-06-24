package pet_finder.dtos.comentario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ComentarioRequestDTO {

    @NotBlank(message = "El comentario tiene que tener texto")
    @Size(max=150, message="Máximo 150 caracteres")
    private String texto;

    @NotNull(message = "Debe estar asociado a una publicacion")
    private Long idPublicacion;

    public ComentarioRequestDTO() {
    }

    public String getTexto() {
        return texto;
    }

    public Long getIdPublicacion() {
        return idPublicacion;
    }

}
