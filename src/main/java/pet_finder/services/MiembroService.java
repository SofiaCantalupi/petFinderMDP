package pet_finder.services;


import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import pet_finder.dtos.miembro.MiembroDetailDTO;
import pet_finder.dtos.miembro.MiembroRequestDTO;
import pet_finder.dtos.miembro.MiembroRequestUpdateDTO;
import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.mappers.MiembroMapper;
import pet_finder.models.Miembro;
import pet_finder.enums.RolUsuario;
import pet_finder.models.Publicacion;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;
import pet_finder.validations.MiembroValidation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiembroService {

    public final MiembroRepository miembroRepository;
    public final MiembroValidation miembroValidation;
    public final PublicacionRepository publicacionRepository;
    public final PublicacionService publicacionService;
    public final PasswordEncoder passwordEncoder;
    public final MiembroMapper miembroMapper;

    public MiembroService(MiembroRepository miembroRepository, MiembroValidation miembroValidation, PublicacionRepository publicacionRepository, PublicacionService publicacionService, PasswordEncoder passwordEncoder, MiembroMapper miembroMapper) {
        this.miembroRepository = miembroRepository;
        this.miembroValidation = miembroValidation;
        this.publicacionRepository = publicacionRepository;
        this.publicacionService = publicacionService;
        this.passwordEncoder = passwordEncoder;
        this.miembroMapper = miembroMapper;
    }


    public MiembroDetailDTO crear(MiembroRequestDTO request){

        Miembro miembro = miembroMapper.aEntidad(request);

        miembroValidation.validarNombre(miembro);
        miembroValidation.validarContrasenia(miembro);
        miembroValidation.validarEmailRegistrado(miembro);

        //Le asigno la nueva contraseña, es la anterior ya validada pero esta vez encriptada.
        miembro.setContrasenia(passwordEncoder.encode(miembro.getContrasenia()));
        Miembro miembroGuardado = miembroRepository.save(miembro);

        return miembroMapper.aDetail(miembroGuardado);
    }

    public List<MiembroDetailDTO> listar(){

        List<Miembro> miembros = miembroRepository.findAll()
                .stream()
                .filter(Miembro::isActivo)      //Muestro solo los activos
                .toList();

        return miembroMapper.deEntidadesAdetails(miembros);
    }

    public Miembro obtenerPorId(Long Id){

        Miembro miembro = miembroRepository.findById(Id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esInactivo(miembro);  //Valido de que el miembro sea activo.

        return miembro;
    }

    public MiembroDetailDTO obtenerDetallePorId(Long id){
        return miembroMapper.aDetail(obtenerPorId(id));
    }

    public Miembro obtenerPorEmail(String email){

        return (miembroRepository.findByEmail(email))
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un miembro con ese mail."));
    }

    //La logica de este metodo es para cambiar el miembro entero, inclusive el email.
    //Por el momento no se usa.
    public MiembroDetailDTO modificarPorId(Long id, MiembroRequestDTO request){

        Miembro miembro = miembroMapper.aEntidad(request);

        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setNombre(miembro.getNombre());
        miembroAModificar.setApellido(miembro.getApellido());
        miembroAModificar.setEmail(miembro.getEmail());

        miembroValidation.validarNombre(miembroAModificar);
        miembroValidation.validarEmailUpdates(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return miembroMapper.aDetail(miembroModificado);
    }


    public MiembroDetailDTO modificarDatos(MiembroRequestUpdateDTO dto, Long id){

        Miembro miembroAModificar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroAModificar.setNombre(dto.getNombre());
        miembroAModificar.setApellido(dto.getApellido());
        miembroValidation.validarNombre(miembroAModificar);

        Miembro miembroModificado = miembroRepository.save(miembroAModificar);
        return miembroMapper.aDetail(miembroModificado);
    }

    //Metodo para hacer administrador a un miembro por su ID.
    public MiembroDetailDTO hacerAdministrador(Long id){

        Miembro miembroAHacerAdmin = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esAdministrador(miembroAHacerAdmin);  //Validamos que el miembro no sea admin.

        miembroAHacerAdmin.setRol(RolUsuario.ADMINISTRADOR);

        Miembro miembroModificado = miembroRepository.save(miembroAHacerAdmin);
        return miembroMapper.aDetail(miembroModificado);
    }

    //Metodo para que los administradores den la baja pasiva a cuentas.
    @Transactional
    public String eliminarPorId(Long id){

        Miembro miembroAEliminar = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontro un miembro con ese ID"));

        miembroValidation.esInactivo(miembroAEliminar);
        miembroAEliminar.setActivo(false);  //Baja pasiva.

        miembroRepository.save(miembroAEliminar);

        //Traigo todas las publicaciones de ese miembro que dimos de baja
        List<Publicacion> publicaciones = publicacionRepository.findByMiembroAndActivoTrue(miembroAEliminar);

        //Por cada publicacion del miembro dado de baja, se da de baja la publicación
        //sus mascotas, y los comentarios de la publicación.
        publicaciones.forEach(publicacionService::eliminar);

        return "Se ha dado de baja con éxito al miembro con ID: " + id + " y a sus publicaciones asociadas.";
    }

}
