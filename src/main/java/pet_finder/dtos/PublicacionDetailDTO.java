package pet_finder.dtos;

import pet_finder.models.Publicacion;

import java.time.LocalDate;
import java.util.List;

public record PublicacionDetailDTO(
        String descripcion,
        LocalDate fecha,
        Long mascotaId,
        UbicacionDetailDTO ubicacion,
        List<ComentarioDetailDTO> comentarios
) {
       public PublicacionDetailDTO(Publicacion publicacion, List<ComentarioDetailDTO> comentarios){
           this(
                   publicacion.getDescripcion(),
                   publicacion.getFecha(),
                   publicacion.getMascota().getId(),
                   new UbicacionDetailDTO(publicacion.getUbicacion()),
                   comentarios
           );
       }
}
