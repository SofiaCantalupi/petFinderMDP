package pet_finder.dtos.publicacion;

import pet_finder.dtos.ubicacion.UbicacionDetailDTO;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.miembro.MiembroDetailDTO;
import pet_finder.models.Publicacion;

import java.time.LocalDate;
import java.util.List;

public record PublicacionDetailDTO(
        Long id,
        Boolean activo, 
        String descripcion,
        LocalDate fecha,

        Long idMiembro,
        String nombreCompleto,

        Long idMascota,
        String nombreMascota,
        String tipoMascota,
        String estadoMascota,
        String urlFoto,

        String ubicacion,
        Double latitud,
        Double longitud,

        List<ComentarioDetailDTO> comentarios
) {
       public PublicacionDetailDTO(Publicacion publicacion, 
                                   MiembroDetailDTO miembro, 
                                   MascotaDetailDTO mascota, 
                                   UbicacionDetailDTO ubicacion, 
                                   List<ComentarioDetailDTO> comentarios){
           this(
                   publicacion.getId(),
                   publicacion.getActivo(),
                   publicacion.getDescripcion(),
                   publicacion.getFecha(),

                   miembro.id(),
                   miembro.nombre() + " " + miembro.apellido(),

                   mascota.id(),
                   mascota.nombre(),
                   mascota.tipoMascota(),
                   mascota.estadoMascota(),
                   mascota.urlFoto(),

                   ubicacion.ubicacion(),
                   ubicacion.latitud(),
                   ubicacion.longitud(),
                   comentarios
           );
       }
}
