package pet_finder.dtos;

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
                   new UbicacionDetailDTO(publicacion.getUbicacion()),
                   comentarios
           );
       }
}
