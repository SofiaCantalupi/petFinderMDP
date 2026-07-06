package pet_finder.dtos.miembro;

import jakarta.validation.constraints.NotBlank;

public class MiembroRequestUpdateDTO {

    @NotBlank(message="Este campo es obligatorio")
    private String nombre;

    @NotBlank(message="Este campo es obligatorio")
    private String apellido;

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
