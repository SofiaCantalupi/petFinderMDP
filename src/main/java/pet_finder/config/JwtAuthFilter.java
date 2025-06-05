package pet_finder.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MiembroUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, MiembroUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtiene el header del request y lo guarda en authHeader.
        final String authHeader = request.getHeader("Authorization");       //Nombre del header.
        final String jwt;
        final String email;

        // 2. Si no hay token o no comienza con Bearer (seria un error), seguir el flujo
        //pero no dejaria acceder a los miembros a rutas que requieren autorización.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;     //Corta este metodo para evitar acciones innecesarias, ya que ya se valido que no hay token.
        }

        // 3. Extraer el token
        jwt = authHeader.substring(7);      //Quita los primeros 7 digitos del token, los cuales son "bearer ".
        email = jwtService.extraerEmail(jwt);       //Guarda el email del miembro en la variable email.

        // 4. Si email es válido y aún no hay autenticación en contexto
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //La segunda condición lo que hace es verificar que el usuario no este autenticado. Si no lo esta
            //Lo hace, y si lo esta no lo vuelve a hacer, ya que no es necesario.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // 5. Validar token
            if (jwtService.validarToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //Este objeto representa el usuario autenticado. Teniendo su userDetails y su rol, sin credenciales en este caso (No necesitamos la contraseña)

                // 6. Autentica como tal al usuario
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        // 7. Seguir con el resto de filtros
        filterChain.doFilter(request, response);
    }

}
