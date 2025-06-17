package pet_finder.dtos;

import pet_finder.models.Publicacion;

import java.util.List;

public record PublicacionDetailDTO(
        Publicacion publicacion,
        MascotaDetailDTO mascotaDTO,
        Long miembroId,
        UbicacionDetailDTO ubicacionDTO,
        List<ComentarioDetailDTO> comentarios
) {
    public PublicacionDetailDTO(
            Publicacion publicacion,
            MascotaDetailDTO mascotaDTO,
            UbicacionDetailDTO ubicacionDTO
    ) {
        this(publicacion, mascotaDTO, publicacion.getIdMiembro(), ubicacionDTO, List.of());
    }
}
