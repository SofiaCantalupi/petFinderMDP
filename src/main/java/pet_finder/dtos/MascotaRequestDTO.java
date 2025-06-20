package pet_finder.dtos;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;

public class MascotaRequestDTO {
    private String nombre;

    @NotNull(message = "El campo \"Estado de la mascota\" es obligatorio.")
    private EstadoMascota estadoMascota;

    @NotNull(message = "El campo \"Tipo de mascota\" es obligatorio.")
    private TipoMascota tipoMascota;

    @URL(message = "La foto debe ser una URL válida")
    private String fotoUrl;

    // Constructor
    public MascotaRequestDTO() {
    }

    // Setters and Getters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public @NotNull(message = "El campo \"Tipo de mascota\" es obligatorio.") TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public @NotNull(message = "El campo \"Estado de la mascota\" es obligatorio.") EstadoMascota getEstadoMascota() {
        return estadoMascota;
    }

    public @URL(message = "La foto debe ser una URL válida") String getFotoUrl() {
        return fotoUrl;
    }

}