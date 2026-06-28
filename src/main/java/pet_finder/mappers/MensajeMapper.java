package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.mensaje.MensajeDetailDTO;
import pet_finder.dtos.mensaje.MensajeRequestDTO;
import pet_finder.models.Mensaje;

import java.util.List;

@Component
public class MensajeMapper implements Mapper<MensajeRequestDTO, MensajeDetailDTO, Mensaje> {

    @Override
    public Mensaje aEntidad(MensajeRequestDTO request) {
        Mensaje mensaje = new Mensaje();
        mensaje.setTexto(request.getTexto());
        return mensaje;
    }

    @Override
    public MensajeDetailDTO aDetail(Mensaje mensaje) {
        return new MensajeDetailDTO(mensaje);
    }

    @Override
    public List<MensajeDetailDTO> deEntidadesAdetails(List<Mensaje> mensajes) {
        return mensajes.stream()
                .map(this::aDetail)
                .toList();
    }
}
