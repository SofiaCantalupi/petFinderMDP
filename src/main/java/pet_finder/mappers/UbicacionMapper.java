package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.ubicacion.UbicacionDetailDTO;
import pet_finder.dtos.ubicacion.UbicacionRequestDTO;
import pet_finder.models.*;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Component
public class UbicacionMapper implements Mapper<UbicacionRequestDTO, UbicacionDetailDTO, Ubicacion> {

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
}
