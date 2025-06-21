package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.mascota.MascotaRequestDTO;
import pet_finder.models.Mascota;

import java.util.List;

@Component
public class MascotaMapper implements Mapper<MascotaRequestDTO, MascotaDetailDTO, Mascota> {

    @Override
    public Mascota aEntidad(MascotaRequestDTO request) {

        // Se crea una nueva Mascota, con los datos recibidos del Request
        Mascota mascota = new Mascota();

        mascota.setEstadoMascota(request.getEstadoMascota());
        mascota.setTipoMascota(request.getTipoMascota());
        mascota.setNombre(request.getNombre());
        mascota.setFotoUrl(request.getFotoUrl());

        // La Mascota siempre es creada con Activo = true
        mascota.setEsActivo(true);

        return mascota;
    }

    @Override
    public MascotaDetailDTO aDetail(Mascota entidad) {

        // Se utiliza constructor del record DetailDTO para retornar un nuevo DetailDTO con los valores de la Mascota recibida
        return new MascotaDetailDTO(entidad);
    }

    @Override
    public List<MascotaDetailDTO> deEntidadesAdetails(List<Mascota> entidades) {

        // Cada Mascota de la lista se mappea a Detail
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }
}
