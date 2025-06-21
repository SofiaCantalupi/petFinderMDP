package pet_finder.dtos.publicacion;

import pet_finder.dtos.ubicacion.UbicacionRequestDTO;

public class PublicacionRequestUpdateDTO {

    public PublicacionRequestUpdateDTO() {
    }

    private String descripcion;
    private UbicacionRequestDTO ubicacion;

    public String getDescripcion() {
        return descripcion;
    }

    public UbicacionRequestDTO getUbicacion() {
        return ubicacion;
    }


}
