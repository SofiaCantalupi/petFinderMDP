package pet_finder.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public class CambiarContraseniaDTO{

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contraseniaVieja;

    @NotBlank(message = "La contraseña nueva es obligatoria")
    private String nuevaContrasenia;


    public String getContraseniaVieja() {
        return contraseniaVieja;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

}

