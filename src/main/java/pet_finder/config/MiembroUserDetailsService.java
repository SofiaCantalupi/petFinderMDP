package pet_finder.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.repositories.MiembroRepository;

public class MiembroUserDetailsService implements UserDetailsService {

    private final MiembroRepository miembroRepository;

    public MiembroUserDetailsService(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsuarioNoEncontradoException {
        Miembro miembro = miembroRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró un usuario registrado con el correo: " + email));
        return new MiembroUserDetails(miembro);
    }

}
