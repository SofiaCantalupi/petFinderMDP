package pet_finder.dtos.ubicacion;

import pet_finder.models.Ubicacion;

public record UbicacionDetailDTO(
        String ubicacion,
        Double latitud,
        Double longitud
) {
    public UbicacionDetailDTO(Ubicacion ubicacion) {
        this(
                ubicacion.getDireccion() + " " + ubicacion.getAltura(),
                ubicacion.getLatitud(),
                ubicacion.getLongitud()
        );
    }
}
