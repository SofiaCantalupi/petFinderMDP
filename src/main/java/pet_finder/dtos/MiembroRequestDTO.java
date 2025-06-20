package pet_finder.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MiembroRequestDTO {

    @NotBlank(message="El nombre es obligatorio")
    private String nombre;

    @NotBlank(message="El apellido es obligatorio")
    private String apellido;

    @Email(message="El correo electronico no es válido")
    @NotBlank(message="El correo electronico es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasenia;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenia() {
        return contrasenia;
    }

}
