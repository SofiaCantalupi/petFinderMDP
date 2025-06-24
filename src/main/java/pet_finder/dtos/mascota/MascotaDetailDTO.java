package pet_finder.dtos.mascota;

import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Mascota;

public record MascotaDetailDTO ( Long id, String nombre, EstadoMascota estadoMascota, TipoMascota tipoMascota, Boolean activo, String fotoUrl) {
    public MascotaDetailDTO(Mascota mascota){
        this(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEstadoMascota(),
                mascota.getTipoMascota(),
                mascota.getEsActivo(),
                mascota.getFotoUrl()
        );
    }
}
