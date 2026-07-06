package pet_finder.validations;

import org.springframework.stereotype.Component;
import pet_finder.dtos.ubicacion.UbicacionRequestDTO;
import pet_finder.exceptions.UbicacionInvalidaException;
import pet_finder.models.Ubicacion;

import java.util.Objects;


@Component
public class UbicacionValidation {

    public UbicacionValidation() {}

    public void esInactivo(Ubicacion ubicacion){
        if (!ubicacion.getActivo()){
            throw new IllegalStateException("La ubicación con ID : "+ubicacion.getId()+" esta inactiva.");
        }
    }


    // Valida que las coordenadas (lat y long) no sean nulas, y que correspondan a la ciudad de Mar del Plata
    public void validarGeocodificacion(Ubicacion ubicacion){

        double latitud = ubicacion.getLatitud();
        double longitud = ubicacion.getLongitud();

        if(ubicacion.getLatitud()== null || ubicacion.getLongitud() == null){
            throw new UbicacionInvalidaException("La ubicación debe tener latitud y longitud.");
        }

        double latMin = -38.15, latMax = -37.90;
        double longMin = -57.70, longMax = -57.50;

        boolean dentroDeMarDelPlata =
                latitud >= latMin && latitud <= latMax &&
                longitud >= longMin && longitud <= longMax;

        if(!dentroDeMarDelPlata){
            throw new UbicacionInvalidaException("Las coordenadas se encuentran fuera de Mar del Plata.");
        }

    }

    //Este metodo se usa para indicar si dos ubicaciones son iguales para ser implementadas
    //en una actualización.
    public boolean contenidoIgualA(Ubicacion original, UbicacionRequestDTO nueva) {
        if (nueva == null) return false;

        return Objects.equals(original.getDireccion(), nueva.getDireccion())
                && Objects.equals(original.getAltura(), nueva.getAltura());
    }

}
