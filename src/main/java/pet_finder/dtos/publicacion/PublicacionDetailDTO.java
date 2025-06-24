package pet_finder.dtos.publicacion;

import pet_finder.dtos.ubicacion.UbicacionDetailDTO;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.models.Mascota;
import pet_finder.models.Publicacion;

import java.time.LocalDate;
import java.util.List;

public record PublicacionDetailDTO(
        String nombreCompleto,
        String descripcion,
        LocalDate fecha,
        Mascota mascota,
        UbicacionDetailDTO ubicacion,
        List<ComentarioDetailDTO> comentarios
) {
       public PublicacionDetailDTO(String nombreCompleto,Publicacion publicacion,Mascota mascota, List<ComentarioDetailDTO> comentarios){
           this(
                   nombreCompleto,
                   publicacion.getDescripcion(),
                   publicacion.getFecha(),
                   mascota,
                   publicacion.getUbicacion() != null ? new UbicacionDetailDTO(publicacion.getUbicacion()) : null,
                   comentarios
           );
       }
}
