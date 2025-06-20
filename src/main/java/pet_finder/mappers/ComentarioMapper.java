package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.models.Comentario;

import java.util.ArrayList;
import java.util.List;

@Component
public class ComentarioMapper implements Mapper <ComentarioRequestDTO, ComentarioDetailDTO, Comentario> {


    @Override
    public Comentario aEntidad(ComentarioRequestDTO request) {

        Comentario comentario = new Comentario();
        comentario.setTexto(request.getTexto());

        return comentario;
    }

    @Override
    public ComentarioDetailDTO aDetail(Comentario comentario) {

        return new ComentarioDetailDTO(comentario);
    }

    @Override
    public List<ComentarioDetailDTO> deEntidadesAdetails(List<Comentario> comentarios) {

        List<ComentarioDetailDTO> detalles = new ArrayList<>();

        return comentarios.stream()
                .map(this::aDetail)
                .toList();
    }


//    public Comentario modificar(Comentario existente, ComentarioRequestDTO request) {
//        existente.setTexto(request.getTexto());
//
//        return existente;
//    }
}
