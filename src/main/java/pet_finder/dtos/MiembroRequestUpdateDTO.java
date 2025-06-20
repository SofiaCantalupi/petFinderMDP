package pet_finder.dtos;

import jakarta.validation.constraints.NotBlank;

public class MiembroRequestUpdateDTO {

    @NotBlank(message="Este campo es obligatorio")
    private String nuevoCampo;

    public @NotBlank(message = "Este campo es obligatorio") String getNuevoCampo() {
        return nuevoCampo;
    }

}
