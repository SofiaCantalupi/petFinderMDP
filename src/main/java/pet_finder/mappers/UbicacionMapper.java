package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.*;
import pet_finder.models.*;
import pet_finder.validations.UbicacionValidation;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Component
public class UbicacionMapper implements Mapper<UbicacionRequestDTO, UbicacionDetailDTO, Ubicacion> {

    // Es necesario tener el validation aca
    private final UbicacionValidation ubicacionValidation;

    public UbicacionMapper (UbicacionValidation ubicacionValidation) {
        this.ubicacionValidation = ubicacionValidation;
    }

    @Override
    public Ubicacion aEntidad(UbicacionRequestDTO request) {

        // todo: Comprobar funcionamiento de la peticion a la api externa geocodificadora

        // Se crea una nueva Ubicacion, con los datos recibidos del Request
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setDireccion(request.getDireccion());
        ubicacion.setAltura(request.getAltura());
        ubicacion.setCiudad(request.getCiudad());
        ubicacion.setRegion(request.getRegion());
        ubicacion.setPais(request.getPais());

        return ubicacion;
    }

    @Override
    public UbicacionDetailDTO aDetail(Ubicacion entidad) {
        // Se utiliza constructor del record DetailDTO
        return new UbicacionDetailDTO(entidad);
    }

    @Override
    public List<UbicacionDetailDTO> deEntidadesAdetails(List<Ubicacion> entidades) {
        // Cada Ubicacion de la lista se mappea a Detail
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }

    // este metodo toma el request y la entidad que se quiere modificar, actualiza los datos en la entidad existente y retorna la entidad modificada.
//    public Ubicacion modificar (Ubicacion entidad, UbicacionRequestDTO request) {
//
//        // todo: Comprobar funcionamiento de la peticion a la api externa geocodificadora
//        // Valida si la Ubicacion recibida por DTO se puede geocodificar (realmente existe)
//        ubicacionValidation.validarGeocodificacion(this.aEntidad(request));
//
//        // Se toma la entidad que se quiere modificar y se actualiza con los datos del RequestDTO
//        entidad.setDireccion(request.getDireccion());
//        entidad.setAltura(request.getAltura());
//        entidad.setCiudad(request.getCiudad());
//        entidad.setRegion(request.getRegion());
//        entidad.setPais(request.getPais());
//
//        return entidad; // retorna la entidad actualizada
//    }

}
