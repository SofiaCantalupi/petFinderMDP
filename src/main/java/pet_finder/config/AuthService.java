package pet_finder.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet_finder.config.dtos.AuthResponseDTO;
import pet_finder.config.dtos.CambiarContraseniaDTO;
import pet_finder.config.dtos.LoginRequestDTO;
import pet_finder.config.dtos.RegistroRequestDTO;
import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.exceptions.FormatoInvalidoException;
import pet_finder.exceptions.MiembroInactivoException;
import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.mappers.MiembroMapper;
import pet_finder.models.Miembro;
import pet_finder.enums.RolUsuario;
import pet_finder.repositories.MiembroRepository;
import pet_finder.validations.MiembroValidation;

@Service
public class AuthService {

    private final MiembroRepository miembroRepository;
    private final MiembroValidation miembroValidation;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MiembroMapper miembroMapper;

    public AuthService(MiembroRepository miembroRepository,
                       MiembroValidation miembroValidation,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService, MiembroMapper miembroMapper) {
        this.miembroRepository = miembroRepository;
        this.miembroValidation = miembroValidation;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.miembroMapper = miembroMapper;
    }

    public MiembroDetailDTO registrar(RegistroRequestDTO request){

        Miembro miembro = new Miembro();

        //No se puede usar el mapper porque es un RegistroRequestDTO, no un MiembroRequestDTO.

        miembro.setNombre(request.getNombre());
        miembro.setApellido(request.getApellido());
        miembro.setEmail(request.getEmail());
        miembro.setContrasenia(request.getContrasenia());
        miembro.setRol(RolUsuario.MIEMBRO);
        miembro.setActivo(true);

        // Validaciones personalizadas
        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);     //Siempre se valida antes de encriptar
        miembroValidation.validarEmailRegistrado(miembro);

        miembro.setContrasenia(passwordEncoder.encode(miembro.getContrasenia()));
        //Le asigno la nueva contraseña, es la anterior ya validada pero esta vez encriptada.

        Miembro guardado = miembroRepository.save(miembro); //Guardo en la base de datos.

        return new MiembroDetailDTO(guardado);  //Con el miembro guardado genero el MiembroDetailDTO con todos los datos.
    }

    public AuthResponseDTO logIn(LoginRequestDTO request){

        //Primero verifico el email asegurandome de que haya un miembro registrado con el mismo ingresado en el LogInRequestDTO
        Miembro miembro = miembroRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un miembro con ese email"));

        //Si lo hay, verifico que este activo (Que no se le haya dado la baja pasiva)
        miembroValidation.esInactivo(miembro);

        //Recien ahora verifico la contraseña.
        if (!passwordEncoder.matches(request.contrasenia(), miembro.getContrasenia())) {
            throw new FormatoInvalidoException("La contraseña no es válida");
        }

        //Si la contraseña es valida, pasamos el miembro a UserDetails para generar el token
        UserDetails miembroUserDetails = new MiembroUserDetails(miembro);

        // se genera el token y se retorna dentro del AuthResponseDTO.
        String token = jwtService.generateToken(miembroUserDetails); // JWT firmado

        return new AuthResponseDTO(token,miembro.getNombre(),miembro.getRol().name());
    }

    public String cambiarContrasenia(CambiarContraseniaDTO request, Long id){

        //Primero me traigo al miembro autenticado a traves del token
        Miembro miembro = miembroRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró al miembro."));

        //Primero verifico que la contraseña actual ingresada por el usuario sea correcta.
        //Uso el passwordEncoder porque la contraseña esta cifrada.
        if (!passwordEncoder.matches(request.getContraseniaVieja(), miembro.getContrasenia())) {
            throw new FormatoInvalidoException("La contraseña actual no coincide con la ingresada.");
        }

        //Si es así, le pongo la contraseña del request al miembro y despues la verifico.
        miembro.setContrasenia(request.getNuevaContrasenia());
        miembroValidation.validarContrasenia(miembro);

        //Una vez validada, la cifro.
        miembro.setContrasenia(passwordEncoder.encode(request.getNuevaContrasenia()));

        //guardo el miembro con la contraseña actualizada en el repositorio
        miembroRepository.save(miembro);

            return "La contraseña se ha cambiado con éxito.";
    }

    }

