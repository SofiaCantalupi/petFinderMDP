package pet_finder.dtos.mascota;

import pet_finder.models.Mascota;

public record MascotaDetailDTO ( Long id, String nombre, String estadoMascota, String tipoMascota, Boolean activo, String urlFoto) {
    public MascotaDetailDTO(Mascota mascota){
        this(
                mascota.getId(),
                mascota.getNombre(),
                mascota.getEstadoMascota().getValorFront(),
                mascota.getTipoMascota().getValorFront(),
                mascota.getEsActivo(),
                mascota.getUrlFoto()
        );
    }
}
