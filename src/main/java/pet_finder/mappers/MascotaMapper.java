package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.MascotaDetailDTO;
import pet_finder.dtos.MascotaRequestDTO;
import pet_finder.models.Mascota;
import pet_finder.models.Miembro;
import pet_finder.validations.MiembroValidation;

import java.util.List;

@Component
public class MascotaMapper implements Mapper<MascotaRequestDTO, MascotaDetailDTO, Mascota> {

    private final MiembroValidation miembroValidation;

    public MascotaMapper(MiembroValidation miembroValidation) {
        this.miembroValidation = miembroValidation;
    }


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

    // este metodo toma el request y la entidad que se quiere modificar, actualiza los datos en la entidad existente y retorna la entidad modificada.
    public Mascota modificar(Mascota entidad, MascotaRequestDTO request,Long idMiembroLogeado) {
        // Se toma la entidad que se quiere modificar y se actualiza con los datos del RequestDTO

        entidad.setNombre(request.getNombre());
        entidad.setEstadoMascota(request.getEstadoMascota());
        entidad.setTipoMascota(request.getTipoMascota());
        entidad.setFotoUrl(request.getFotoUrl());
        miembroValidation.estaLogeado(entidad.getMiembroId(),idMiembroLogeado);
        // Como se trabaja con la entidad a modificar, no hace falta settear esActivo como true;
        // Antes de modificarla, se valida que el registro se encuentre activo, sino lanza error
        // entidad.setEsActivo(true); -> No es necesario

        return entidad; // retorna la entidad actualizada
    }
}
