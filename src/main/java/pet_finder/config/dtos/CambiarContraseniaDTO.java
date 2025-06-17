package pet_finder.config.dtos;

import jakarta.validation.constraints.NotBlank;

public class CambiarContraseniaDTO{

    @NotBlank(message = "La contraseña actual es obligatoria")
    private String contraseniaVieja;

    @NotBlank(message = "La contraseña nueva es obligatoria")
    private String nuevaContrasenia;


    public String getContraseniaVieja() {
        return contraseniaVieja;
    }
    public void setContraseniaVieja(String contraseniaVieja) {
        this.contraseniaVieja = contraseniaVieja;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }
    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

}

