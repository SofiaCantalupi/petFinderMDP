package pet_finder.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.exceptions.formatoInvalidoException;
import pet_finder.exceptions.miembroInactivoException;
import pet_finder.exceptions.usuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.models.Rol;
import pet_finder.repositories.MiembroRepository;
import pet_finder.validations.MiembroValidation;

@Service
public class AuthService {

    private final MiembroRepository miembroRepository;
    private final MiembroValidation miembroValidation;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(MiembroRepository miembroRepository,
                       MiembroValidation miembroValidation,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.miembroRepository = miembroRepository;
        this.miembroValidation = miembroValidation;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public MiembroDetailDTO registrar(RegistroRequestDTO request){
        Miembro miembro = new Miembro();

        miembro.setNombre(request.getNombre());
        miembro.setApellido(request.getApellido());
        miembro.setEmail(request.getEmail());
        miembro.setContrasenia(request.getContrasenia());

        // Validaciones personalizadas
        miembroValidation.validarNombre(miembro);
        miembroValidation.validarApellido(miembro);
        miembroValidation.validarContrasenia(miembro);     //Siempre se valida antes de encriptar
        miembroValidation.validarEmailRegistrado(miembro);

        miembro.setContrasenia(passwordEncoder.encode(miembro.getContrasenia()));
        //Le asigno la nueva contraseña, es la anterior ya validada pero esta vez encriptada.

        miembro.setRol(Rol.MIEMBRO);        //Datos por defecto
        miembro.setActivo(true);

        Miembro guardado = miembroRepository.save(miembro); //Guardo en la base de datos.


        return new MiembroDetailDTO(guardado);  //Con el miembro guardado genero el MiembroDetailDTO con todos los datos.
    }

    public AuthResponseDTO logIn(LoginRequestDTO request){

        //Primero verifico el email asegurandome de que haya un miembro registrado con el mismo ingresado en el LogInRequestDTO
        Miembro miembro = miembroRepository.findByEmail(request.email())
                .orElseThrow(() -> new usuarioNoEncontradoException("No se encontró un miembro con ese email"));

        //Si lo hay, verifico que este activo (Que no se le haya dado la baja pasiva)
        if (!miembro.isActivo()) {
            throw new miembroInactivoException("El miembro está inactivo.");
        }

        //Recien ahora verifico la contraseña.
        if (!passwordEncoder.matches(request.contrasenia(), miembro.getContrasenia())) {
            throw new formatoInvalidoException("La contraseña no es válida");
        }

        //Si la contraseña es valida, se genera el token y se retorna dentro del AuthResponseDTO.
        String token = jwtService.generateToken(miembro); // JWT firmado

        return new AuthResponseDTO(token, miembro.getNombre(), miembro.getRol().name());
    }

    }
}
