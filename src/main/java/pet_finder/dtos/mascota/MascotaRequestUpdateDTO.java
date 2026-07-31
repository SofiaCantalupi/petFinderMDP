package pet_finder.dtos.mascota;

import org.hibernate.validator.constraints.URL;
import pet_finder.enums.TipoMascota;

public class MascotaRequestUpdateDTO {

    // Los campos son opcionales, con el objetivo de modificar solo los atributos necesarios
    private String nombre;
    private TipoMascota tipoMascota;

    @URL(message = "Debe ingresar una URL válida.")
    private String urlFoto;

    public String getNombre() {
        return nombre;
    }

    public TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public @URL(message = "Debe ingresar una URL válida.") String getUrlFoto() {
        return urlFoto;
    }
}
