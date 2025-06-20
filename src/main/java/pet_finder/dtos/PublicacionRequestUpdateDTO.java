package pet_finder.dtos;

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
