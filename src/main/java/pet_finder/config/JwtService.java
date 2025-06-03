package pet_finder.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import pet_finder.models.Miembro;

import java.security.Key;


@Service

public class JwtService {

    //Esta es una KEY secreta que tenemos que guardar. Con esta key se generan todos los Tokens y al tenerla secreta nos aseguramos
    //que los tokens no puedan ser trucados. Solo con esta key se pueden generar y validar tokens LEGITIMOS.

    private static final String SECRET_KEY = "mi-clave-secreta-super-segura-para-jwt-1234567890abcd";

    //Este metodo convierte la constante SECRET_KEY en un objeto de clase Key que se usa para "crear y validar" (firmar y verificar) los tokens
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Miembro miembro){
        return "token";
    }
}
