package pet_finder.services;

import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.dtos.MiembroRequestDTO;
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
    public final MiembroMapper miembroMapper;

    public MiembroService(MiembroRepository miembroRepository, MiembroValidation miembroValidation,MiembroMapper miembroMapper) {
        this.miembroRepository = miembroRepository;
        this.miembroValidation = miembroValidation;
        this.miembroMapper = miembroMapper;
    }


    public Miembro crear(Miembro miembro){

        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);       //Probablemente acá tenga que hacer el traslado con SpringSecurity de la password.
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

        miembroValidation.esInactivo(miembro);

        return miembro;
    }

    public Miembro obtenerPorEmail(String email){
        return (miembroRepository.findByEmail(email))
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un miembro con ese mail."));
    }

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

    public Miembro hacerAdministrador(Long id){
        Miembro miembroAHacerAdmin = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esAdministrador(miembroAHacerAdmin);  //Validamos que el miembro no sea admin.

        miembroAHacerAdmin.setRol(RolUsuario.ADMINISTRADOR);

        Miembro miembroModificado = miembroRepository.save(miembroAHacerAdmin);
        return miembroModificado;
    }


    public String eliminarPorId(Long id){
        Miembro miembroAEliminar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esInactivo(miembroAEliminar);
        miembroAEliminar.setActivo(false);

        miembroRepository.save(miembroAEliminar);

            return "Se ha dado de baja con éxito al miembro con ID: " + id;
    }

    public String eliminarPorEmail(String email){

        Miembro miembroAEliminar = miembroRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese correo electronico"));

        miembroValidation.esInactivo(miembroAEliminar);
        miembroAEliminar.setActivo(false);

        miembroRepository.save(miembroAEliminar);

        return "Se ha dado de baja con éxito al miembro con el correo: " + email;
    }

}
