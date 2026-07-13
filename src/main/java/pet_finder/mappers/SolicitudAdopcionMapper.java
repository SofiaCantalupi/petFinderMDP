package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.models.SolicitudAdopcion;

import java.util.List;

@Component
public class SolicitudAdopcionMapper implements Mapper <SolicitudAdopcionRequestDTO, SolicitudAdopcionDetailDTO, SolicitudAdopcion>{


    @Override
    public SolicitudAdopcion aEntidad(SolicitudAdopcionRequestDTO request) {
        SolicitudAdopcion solicitudAdopcion = new SolicitudAdopcion();

        solicitudAdopcion.setCelular(request.getCelular());
        solicitudAdopcion.setMotivoAdopcion(request.getMotivoAdopcion());
        solicitudAdopcion.setAceptaCondiciones(request.isAceptaCondiciones());
        solicitudAdopcion.setTipoHogar(request.getTipoHogar());
        solicitudAdopcion.setHayMascotaEnHogar(request.getHayMascotaEnHogar());
        solicitudAdopcion.setTipoMascotasEnHogar(request.getTipoMascotasEnHogar());
        solicitudAdopcion.setTienePatio(request.getTienePatio());

        return solicitudAdopcion;
    }

    @Override
    public SolicitudAdopcionDetailDTO aDetail(SolicitudAdopcion solicitudAdopcion) {
        return new SolicitudAdopcionDetailDTO(solicitudAdopcion);
    }

    @Override
    public List<SolicitudAdopcionDetailDTO> deEntidadesAdetails(List<SolicitudAdopcion> entidades) {
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }
}
