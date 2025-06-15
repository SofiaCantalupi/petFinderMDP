package pet_finder.dtos;

import pet_finder.models.Publicacion;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Daniel Herrera
 */

public record PublicacionDetailDTO (
        Long id,
        String descripcion,
        LocalDate fecha,
        Boolean activo,

        MascotaDetailDTO mascotaDTO,

        // todo: Considerar cambiar a miembroDetailDTO
        MiembroDetailDTO miembroDTO,

        // todo: Considerar cambiar a ubicacionDetailDTO
        UbicacionDetailDTO ubicacionDTO,

        List<ComentarioDetailDTO> comentarios
) {
    public PublicacionDetailDTO(Publicacion publicacion, MascotaDetailDTO mascotaDTO, MiembroDetailDTO miembroDTO, UbicacionDetailDTO ubicacionDTO, List<ComentarioDetailDTO> comentariosDTO){
        this(
                publicacion.getId(),
                publicacion.getDescripcion(),
                publicacion.getFecha(),
                publicacion.getActivo(),
                mascotaDTO,
                miembroDTO,
                ubicacionDTO,
                comentariosDTO
        );
    }
}