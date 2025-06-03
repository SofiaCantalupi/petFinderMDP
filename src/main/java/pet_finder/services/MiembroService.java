package pet_finder.services;

import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.dtos.MiembroRequestDTO;
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


    public MiembroDetailDTO crear(MiembroRequestDTO request){

        Miembro miembro = new Miembro();

        miembro.setNombre(request.getNombre());
        miembro.setApellido(request.getApellido());
        miembro.setEmail(request.getEmail());
        miembro.setContrasenia(request.getContrasenia());
        miembro.setRol(Rol.MIEMBRO);

        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);       //Probablemente acá tenga que hacer el traslado con SpringSecurity de la password.
        miembroValidation.validarEmailRegistrado(miembro);

        Miembro miembroGuardado = miembroRepository.save(miembro);

        return new MiembroDetailDTO(miembroGuardado);
    }

    public List<Miembro> listar(){
        return miembroRepository.findAll();
    }

    public MiembroDetailDTO obtenerPorId(Long Id){
        Miembro miembro = miembroRepository.findById(Id)
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontro un miembro con ese ID"));
        return new MiembroDetailDTO(miembro.getId(),miembro.getNombre(),miembro.getApellido(),miembro.getEmail(),miembro.getRol().name());
    }

    public Miembro obtenerPorEmail(String email){
        return (miembroRepository.findByEmail(email))
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontró un miembro con ese mail."));
    }

    public MiembroDetailDTO modificarPorId(Long id,MiembroRequestDTO request){
        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setNombre(request.getNombre());
        miembroAModificar.setApellido(request.getApellido());
        miembroAModificar.setEmail(request.getEmail());

        miembroValidation.validarNombre(miembroAModificar);
        miembroValidation.validarApellido(miembroAModificar);
        miembroValidation.validarEmailUpdates(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return new MiembroDetailDTO(miembroModificado);
    }

    public MiembroDetailDTO hacerAdministrador(Long id){
        Miembro miembroAHacerAdmin = miembroRepository.findById(id)
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esAdministrador(miembroAHacerAdmin);  //Validamos que el miembro no sea admin.

        miembroAHacerAdmin.setRol(Rol.ADMINISTRADOR);

        Miembro miembroModificado = miembroRepository.save(miembroAHacerAdmin);
        return new MiembroDetailDTO(miembroModificado);
    }


    public String eliminarPorId(Long Id){
        miembroValidation.validarExistenciaPorId(Id);

            miembroRepository.deleteById(Id);
            return "Se ha eliminado con éxito al miembro con ID: " + Id;
    }

    public String eliminarPorEmail(String email){
        miembroValidation.validarExistenciaPorEmail(email);

        miembroRepository.deleteByEmail(email);
        return "Se ha eliminado con éxito al miembro con email: " + email;
    }


}
