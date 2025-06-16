package pet_finder.dtos;

import pet_finder.models.Ubicacion;

/**
 * @author Daniel Herrera
 */

public record UbicacionDetailDTO(
        Long id,
        String direccion,
        Integer altura,
        String ciudad,
        String region, // Estado, provincia o departamento
        String pais,
        Boolean activo
) {
    public UbicacionDetailDTO(Ubicacion ubicacion){
        this(
                ubicacion.getId(),
                ubicacion.getDireccion(),
                ubicacion.getAltura(),
                ubicacion.getCiudad(),
                ubicacion.getRegion(),
                ubicacion.getPais(),
                ubicacion.getActivo()
        );
    }
}
