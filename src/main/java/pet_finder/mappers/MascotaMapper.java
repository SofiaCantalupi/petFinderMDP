package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.MascotaDetailDTO;
import pet_finder.dtos.MascotaRequestDTO;
import pet_finder.models.Mascota;

import java.util.List;

@Component
public class MascotaMapper implements Mapper<MascotaRequestDTO, MascotaDetailDTO, Mascota> {

    @Override
    public Mascota aEntidad(MascotaRequestDTO request) {
        Mascota mascota = new Mascota();

        mascota.setEstadoMascota(request.getEstadoMascota());
        mascota.setTipoMascota(request.getTipoMascota());
        mascota.setDescripcion(request.getDescripcion());
        mascota.setNombre(request.getNombre());

        if (request.getActivo() != null) {
            mascota.setActivo(request.getActivo());
        }

        return mascota;
    }

    @Override
    public MascotaDetailDTO aDetail(Mascota entidad) {
        return new MascotaDetailDTO(entidad);
    }

    @Override
    public List<MascotaDetailDTO> deEntidadesAdetails(List<Mascota> entidades) {
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }

    @Override
    public Mascota modificar(Mascota entidad, MascotaRequestDTO request) {
        entidad.setNombre(request.getNombre());
        entidad.setEstadoMascota(request.getEstadoMascota());
        entidad.setTipoMascota(request.getTipoMascota());
        entidad.setDescripcion(request.getDescripcion());

        // si el estado activo del request es distinto de null, se settea el valor del request, sino en de la entidad existente
        entidad.setActivo(request.getActivo() != null ? request.getActivo() : entidad.getActivo());

        return entidad;
    }
}
