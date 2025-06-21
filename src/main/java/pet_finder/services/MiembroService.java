package pet_finder.services;


import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.mappers.MiembroMapper;
import pet_finder.models.Miembro;
import pet_finder.enums.RolUsuario;
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


    public Miembro crear(Miembro miembro){

        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);
        miembroValidation.validarEmailRegistrado(miembro);

        Miembro miembroGuardado = miembroRepository.save(miembro);

        return miembroGuardado;
    }

    public List<Miembro> listar(){

        return miembroRepository.findAll()
                .stream()
                .filter(Miembro::isActivo)      //Muestro solo los activos
                .toList();
    }

    public Miembro obtenerPorId(Long Id){

        Miembro miembro = miembroRepository.findById(Id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esInactivo(miembro);  //Valido de que el miembro sea activo.

        return miembro;
    }

    public Miembro obtenerPorEmail(String email){

        return (miembroRepository.findByEmail(email))
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un miembro con ese mail."));
    }

    //La logica de este metodo es para cambiar el miembro entero, inclusive el email.
    //Por el momento no se usa.
    public Miembro modificarPorId(Long id,Miembro miembro){

        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setNombre(miembro.getNombre());
        miembroAModificar.setApellido(miembro.getApellido());
        miembroAModificar.setEmail(miembro.getEmail());

        miembroValidation.validarNombre(miembroAModificar);
        miembroValidation.validarApellido(miembroAModificar);
        miembroValidation.validarEmailUpdates(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return miembroModificado;
    }

    public Miembro modificarNombre(String nombre,Long id){

        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setNombre(nombre);
        miembroValidation.validarNombre(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return miembroModificado;
    }

    public Miembro modificarApellido(String apellido, Long id){

        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setApellido(apellido);
        miembroValidation.validarApellido(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return miembroModificado;
    }

    //Metodo para hacer administrador a un miembro por su ID.
    public Miembro hacerAdministrador(Long id){

        Miembro miembroAHacerAdmin = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esAdministrador(miembroAHacerAdmin);  //Validamos que el miembro no sea admin.

        miembroAHacerAdmin.setRol(RolUsuario.ADMINISTRADOR);

        Miembro miembroModificado = miembroRepository.save(miembroAHacerAdmin);
        return miembroModificado;
    }

    //Metodo para que los administradores den la baja pasiva a cuentas.
    public String eliminarPorId(Long id){
        Miembro miembroAEliminar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esInactivo(miembroAEliminar);
        miembroAEliminar.setActivo(false);  //Baja pasiva.

        miembroRepository.save(miembroAEliminar);

            return "Se ha dado de baja con éxito al miembro con ID: " + id;
    }

    //Metodo para que los usuarios den de baja pasiva su propia cuenta.
    public String eliminarPorEmail(String email){

        Miembro miembroAEliminar = miembroRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese correo electronico"));

        miembroValidation.esInactivo(miembroAEliminar);
        miembroAEliminar.setActivo(false);

        miembroRepository.save(miembroAEliminar);

        return "Diste de baja con éxito tu cuenta con el correo: " + email;
    }

}
