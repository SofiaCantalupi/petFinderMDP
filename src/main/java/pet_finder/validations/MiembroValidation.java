package pet_finder.validations;

import pet_finder.exceptions.emailYaRegistradoException;
import pet_finder.exceptions.errorEnRolException;
import pet_finder.exceptions.formatoInvalidoException;
import pet_finder.exceptions.usuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.models.Rol;
import pet_finder.repositories.MiembroRepository;
import org.springframework.stereotype.Component;
import pet_finder.repositories.MiembroRepository;

@Component
public class MiembroValidation {

    public final MiembroRepository miembroRepository;

    public MiembroValidation(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    public void validarNombre(Miembro miembro){
        String regex = "^[A-Za-zñÑáéíóúÁÉÍÓÚ ]{3,15}$";
        //Hace que puedan ser mayus o minus, con tildes y ñ, de 3 a 15 caracteres.
        if(!miembro.getNombre().matches(regex)){
            throw new formatoInvalidoException("El nombre no cumple con el formato. Recordá que no debe contener numeros y su longitud debe ser de 3 a 15 caracteres.");
        }
    }

    public void validarApellido(Miembro miembro){
        String regex = "^[A-Za-zñÑáéíóúÁÉÍÓÚ ]{3,15}$";
        //Hace que puedan ser mayus o minus, con tildes y ñ, de 3 a 15 caracteres.
        if(!miembro.getApellido().matches(regex)){
            throw new formatoInvalidoException("El apellido no cumple con el formato. Recordá que no debe contener numeros y su longitud debe ser de 3 a 15 caracteres.");
        }
    }
    public void validarContrasenia(Miembro miembro){
        String regexContrasenia = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{6,15}$";
        if(!miembro.getContrasenia().matches(regexContrasenia)){
            throw new formatoInvalidoException("La contraseña no cumple con el formato. Recordá que debe tener como minimo una letra mayuscula," +
                    " una letra miniscula, un numero, un caracter especial(Por ejemplo: !$%&_#) y su longitud debe ser de 6 a 15 caracteres.");
        }
    }
    public void validarEmailRegistrado(Miembro miembro) {
        miembroRepository.findByEmail(miembro.getEmail())
                .ifPresent(miembroExistente -> {
                    throw new emailYaRegistradoException("El mail ingresado ya existe en la base de datos.");
                });
    }

    public void validarExistenciaPorId(Long Id){
            if(miembroRepository.findById(Id).isEmpty()){
                throw new usuarioNoEncontradoException("No se encontró un usuario con el ID: " + Id);
            }
    }

    public void validarExistenciaPorEmail(String email){
        if(miembroRepository.findByEmail(email).isEmpty()){
            throw new usuarioNoEncontradoException("No se encontró un usuario con el email: " + email);
        }
    }

    public void validarEmailUpdates(Miembro miembro){       //Testear
        boolean existe = miembroRepository.existsByEmailAndIdNot(miembro.getEmail(),miembro.getId());
        if(existe){
            throw new emailYaRegistradoException("El correo electronico actualizado ya esta registrado por otro usuario.");
        }
    }

    public void esAdministrador(Miembro miembro){
        if (miembro.getRol() == Rol.ADMINISTRADOR){
            throw new errorEnRolException("El miembro ya es un administrador.");
        }
    }

}
