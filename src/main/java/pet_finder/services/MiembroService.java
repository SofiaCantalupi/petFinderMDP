package pet_finder.services;

import pet_finder.exceptions.usuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.models.Rol;
import pet_finder.repositories.MiembroRepository;
import pet_finder.validations.MiembroValidation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiembroService {

    public final MiembroRepository miembroRepository;
    public final MiembroValidation miembroValidation;

    public MiembroService(MiembroRepository miembroRepository, MiembroValidation miembroValidation) {
        this.miembroRepository = miembroRepository;
        this.miembroValidation = miembroValidation;
    }


    public Miembro altaMiembro(Miembro miembro){

        //Validaciones
        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);       //Probablemente acá tenga que hacer el traslado con SpringSecurity de la password.
        miembroValidation.validarEmailRegistrado(miembro);

        miembro.setRol(Rol.MIEMBRO);        //Asegura de que por defecto son miembros.  //Hariamos uno aparte de admins?

        return miembroRepository.save(miembro);
    }

    public List<Miembro> listarMiembros(){
        return miembroRepository.findAll();
    }

    public Miembro obtenerMiembroPorId(Long Id){
        return (miembroRepository.findById(Id))
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontro un miembro con ese ID"));
    }

    public Miembro obtenerMiembroPorEmail(String email){
        return (miembroRepository.findByEmail(email))
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontró un miembro con ese mail."));
    }


    public String borrarMiembroPorId(Long Id){
        miembroValidation.validarExistenciaPorId(Id);   //Si no existe tira la exception acá.

            miembroRepository.deleteById(Id);
            return "Se ha eliminado con éxito al miembro con ID: " + Id;
    }

    public String borrarMiembroPorEmail(String email){
        miembroValidation.validarExistenciaPorEmail(email);   //Si no existe tira la exception acá.

        miembroRepository.deleteByEmail(email);
        return "Se ha eliminado con éxito al miembro con email: " + email;
    }

}
