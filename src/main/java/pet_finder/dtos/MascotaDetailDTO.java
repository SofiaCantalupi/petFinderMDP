package pet_finder.dtos;

import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Mascota;

public record MascotaDetailDTO ( Long id, String nombre, EstadoMascota estadoMascota, TipoMascota tipoMascota, String descripcion, Boolean activo) {
    public MascotaDetailDTO(Mascota mascota){
        this(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEstadoMascota(),
                mascota.getTipoMascota(),
                mascota.getDescripcion(),
                mascota.getActivo()
        );
    }
}
