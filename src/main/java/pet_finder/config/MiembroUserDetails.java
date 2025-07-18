package pet_finder.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pet_finder.models.Miembro;

import java.util.Collection;
import java.util.List;

public class MiembroUserDetails implements UserDetails {

    private final Miembro miembro;

    public MiembroUserDetails(Miembro miembro) {
        this.miembro = miembro;
    }

    //Trae el rol del miembro autenticado.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + miembro.getRol().name()));
    }

    public Long getId() {
        return miembro.getId();
    }

    @Override
    public String getPassword() {
        return miembro.getContrasenia();
    }

    public String getNombre() {
        return miembro.getNombre();
    }

    public String getApellido() {
        return miembro.getApellido();
    }

    @Override
    public String getUsername() {
        return miembro.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return miembro.isActivo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return miembro.isActivo();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return miembro.isActivo();
    }

    @Override
    public boolean isEnabled() {
        return miembro.isActivo();
    }
}
