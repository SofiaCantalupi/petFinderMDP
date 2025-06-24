package pet_finder.dtos.ubicacion;

import pet_finder.models.Ubicacion;

public record UbicacionDetailDTO(
        String direccion,
        Integer altura
) {
    public UbicacionDetailDTO(Ubicacion ubicacion) {
        this(
                ubicacion.getDireccion(),
                ubicacion.getAltura()
        );
    }
}
