package pet_finder.config;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet_finder.config.dtos.AuthResponseDTO;
import pet_finder.config.dtos.CambiarContraseniaDTO;
import pet_finder.config.dtos.LoginRequestDTO;
import pet_finder.config.dtos.RegistroRequestDTO;
import pet_finder.dtos.MiembroDetailDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Registro de usuario
    @PostMapping("/registro")
    public ResponseEntity<MiembroDetailDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {

        MiembroDetailDTO nuevoMiembro = authService.registrar(request);     //Le paso el registro y devuelvo el miembro Creado (Un DetailDTO)
        return ResponseEntity.ok(nuevoMiembro);
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        //El login devuelve un AuthResponseDTO donde se encuentra el token valido para
        //estar autenticado.
        return ResponseEntity.ok(authService.logIn(request));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @PostMapping("/cambiar-contrasenia")
    public ResponseEntity<String> cambiarContrasenia(@Valid @RequestBody CambiarContraseniaDTO request, @AuthenticationPrincipal MiembroUserDetails userDetails) {
        return ResponseEntity.ok(authService.cambiarContrasenia(request, userDetails.getId()));
    }


}

