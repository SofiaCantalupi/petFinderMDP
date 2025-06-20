package pet_finder.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;


@Service
public class JwtService {

    //Esta es una KEY secreta que tenemos que guardar. Con esta key se generan todos los Tokens y al tenerla secreta nos aseguramos
    //que los tokens no puedan ser trucados. Solo con esta key se pueden generar y validar tokens LEGITIMOS.
    //Por seguridad se guarda en una variable de entorno para no exponerla.
    private final String SECRET_KEY;

    //Lee desde el properties el valor de la key, que esta vinculada a una variable de entorno
    //definida en el IDE de cada uno (JWT_SECRET)
    //El metodo de abajo hace que se guarde en el string secretKey la variable de entorno del IDE y se guarde en SECRET_KEY
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    //Este metodo convierte la constante SECRET_KEY en un objeto de clase Key que se usa para "crear y validar" (firmar y verificar) los tokens
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    //Generación del token sin especificaciones.
    //Este token va a ser el que use el usuario para navegar por la aplicación web una vez logeado.
    //Este token nomás contiene el mail del usuario.
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()           //builder inicializa la construccion del token
                .setSubject(userDetails.getUsername())       // Llama al metodo getUsername del userDetails, que en realidad obtiene el mail del miembro
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Fecha de creación del token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // Fecha de expiración del token, 1 hora.(+1 segundo * 60 segundos * 60 minutos)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Firmar con clave secreta usando HS256
                .compact();    // Compacta el JWT en String
    }

    //Generación del token (con claim), el cual contendria datos adicionales
    public String generarTokenConClaims(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Trae las claims de un token
    public Claims extraerTodasClaims(String token) {
        return Jwts.parserBuilder()     //ParserBuilder se usa para obtener la información del token.
                .setSigningKey(getSignInKey())   // Usa la clave para validar la firma
                .build()
                .parseClaimsJws(token)           // Parsea el token
                .getBody();                     // Obtiene el cuerpo con los claims
    }

    //Extrae el mail del usuario desde el token.
    public String extraerEmail(String token) {
        return extraerTodasClaims(token).getSubject();
    }

    //Se fija si el token ya expiró.
    private Boolean esTokenExpirado(String token) {
        return extraerTodasClaims(token).getExpiration().before(new Date());
    }

    //Metodo para validar que el usuario logeado coincida con el del token y que no este expirado
    public Boolean validarToken(String token, UserDetails userDetails) {
        final String email = extraerEmail(token);
        return (email.equals(userDetails.getUsername()) && !esTokenExpirado(token));
    }


}

