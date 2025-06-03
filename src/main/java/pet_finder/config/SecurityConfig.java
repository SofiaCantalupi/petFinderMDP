package pet_finder.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{     //Acá voy a especificar a que rutas se va a poder acceder y a cuales con auth.
        return http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/auth/**").permitAll()
                //Con este metodo pongo que voy a permitir el acceso a todas las rutas que comiencen con
                // /auth. Ya que van a ser el registro y el log in, y estas rutas tienen que ser publicas.
                        .anyRequest().authenticated()
                //Y acá le digo que para las demas rutas, se necesita estar autentificado.

        )
                .csrf(csrf -> csrf.disable())           //No util para SpringBoot (En este caso)
                .httpBasic(Customizer.withDefaults())       //Esto hace que se tenga que "logear" para autentificarse.
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){       //Esto nos va a servir para encriptar las contraseñas, requisito de SpringSecurity.
        return new BCryptPasswordEncoder();
    }

}
