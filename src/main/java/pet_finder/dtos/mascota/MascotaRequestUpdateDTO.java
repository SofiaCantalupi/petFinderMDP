package pet_finder.dtos.mascota;

import org.hibernate.validator.constraints.URL;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;

public class MascotaRequestUpdateDTO {

    // Los campos son opcionales, con el objetivo de modificar solo los atributos necesarios
    private String nombre;
    private EstadoMascota estadoMascota;
    private TipoMascota tipoMascota;

    @URL(message = "Debe ingresar una URL válida.")
    private String fotoUrl;

    public String getNombre() {
        return nombre;
    }

    public EstadoMascota getEstadoMascota() {
        return estadoMascota;
    }

    public TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public @URL(message = "Debe ingresar una URL válida.") String getFotoUrl() {
        return fotoUrl;
    }
}
