package pet_finder.dtos.publicacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import pet_finder.dtos.ubicacion.UbicacionRequestDTO;

public class PublicacionRequestUpdateDTO {

    public PublicacionRequestUpdateDTO() {
    }

    @NotBlank(message = "La descripcion tiene que tener texto")
    @Size(max=1500, message="Máximo 1500 caracteres")
    private String descripcion;

    @Valid
    private UbicacionRequestDTO ubicacion;

    public String getDescripcion() {
        return descripcion;
    }

    public UbicacionRequestDTO getUbicacion() {
        return ubicacion;
    }


}
