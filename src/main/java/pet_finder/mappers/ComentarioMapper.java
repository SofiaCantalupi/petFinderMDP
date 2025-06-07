package pet_finder.mappers;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.models.Comentario;
import pet_finder.models.Mascota;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;

import java.util.ArrayList;
import java.util.List;


@Component
public class ComentarioMapper implements Mapper <ComentarioRequestDTO, ComentarioDetailDTO, Comentario> {




    @Override
    public Comentario aEntidad(ComentarioRequestDTO request) {

        Comentario comentario = new Comentario();
        comentario.setTexto(request.getTexto());
        comentario.setFechaPublicacion(request.getFechaPublicacion());


        return comentario;
    }



    @Override
    public ComentarioDetailDTO aDetail(Comentario comentario) {
        return new ComentarioDetailDTO(comentario);
    }



    @Override
    public List<ComentarioDetailDTO> deEntidadesAdetails(List<Comentario> comentarios) {
        return comentarios.stream()
                .map(this::aDetail)
                .toList();
    }





    @Override
    public Comentario modificar(Comentario existente, ComentarioRequestDTO request) {
        existente.setTexto(request.getTexto());
        existente.setFechaPublicacion(request.getFechaPublicacion());

        return existente;
    }
}
