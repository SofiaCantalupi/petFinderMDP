package pet_finder.validations;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pet_finder.dtos.UbicacionRequestDTO;
import pet_finder.exceptions.MiembroInactivoException;
import pet_finder.exceptions.UbicacionInvalidaException;
import pet_finder.models.Ubicacion;

/**
 * @author Daniel Herrera
 */
@Component
public class UbicacionValidation {

    public UbicacionValidation() {}

    public void esInactivo(Ubicacion ubicacion){
        if (!ubicacion.getActivo()){
            throw new IllegalStateException("La Ubicacion con ID : "+ubicacion.getId()+" esta inactiva.");
        }
    }

    // todo: Comprobar funcionamiento de esta FUNCION
    // FUNCION AUXILIAR VALIDA QUE LA UBICACION ENVIADA EXISTA REALMENTE
    public void validarGeocodificacion(Ubicacion ubicacion) {


        // Arma una cadena (ej: "Rivadavia 3470, Mar del Plata, Buenos Aires, Argentina")
        String query = String.format("%s %s, %s, %s, %s",
                ubicacion.getDireccion(),
                ubicacion.getAltura(),
                ubicacion.getCiudad(),
                ubicacion.getRegion(),
                ubicacion.getPais()
        );

        // Construye la URL para consultar al servicio Nominatim (OpenStreetMap)
        String url = UriComponentsBuilder
                .fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .build()
                .toUriString();

        // Crea un cliente para hacer la petición HTTP
        RestTemplate restTemplate = new RestTemplate();

        // Configura la cabecera "User-Agent" (es obligatoria para que el servidor acepte la petición)
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "mi-app");

        // Crea una entidad HTTP con las cabeceras preparadas
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Hace la consulta (GET) a la URL con las cabeceras
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        // La función devuelve true si:
        // - La respuesta fue exitosa (status 200)
        // - El cuerpo de la respuesta no es nulo
        // - La respuesta no está vacía (es decir, encontró al menos una ubicación)
        boolean ubicacionReal = response.getStatusCode() == HttpStatus.OK &&
                response.getBody() != null &&
                !response.getBody().equals("[]");

        // SI NO se pudo geocodificar (direccion inexistente)
        if (!ubicacionReal) {
            throw new UbicacionInvalidaException("La Ubicacion '"+query+"' no se pudo localizar.");
        }
    }

}
