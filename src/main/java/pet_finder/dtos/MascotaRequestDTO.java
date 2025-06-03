package pet_finder.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;

public class MascotaRequestDTO {
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String nombre;

    @NotNull(message = "El campo \"Estado de la mascota\" es obligatorio.")
    private EstadoMascota estadoMascota;

    @NotNull(message = "El campo \"Tipo de mascota\" es obligatorio.")
    private TipoMascota tipoMascota;

    @Size(max = 1000, message = "La descripción no debe superar los 1000 caracteres." )
    @NotBlank(message = "El campo \"Descripción\" es obligatorio.")
    private String descripcion;

    @NotNull
    private Boolean activo;

    // Constructor
    public MascotaRequestDTO() {
    }

    // Setters and Getters

    public @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.") String getNombre() {
        return nombre;
    }

    public void setNombre(@Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.") String nombre) {
        this.nombre = nombre;
    }

    public @NotNull(message = "El campo \"Estado de la mascota\" es obligatorio.") EstadoMascota getEstadoMascota() {
        return estadoMascota;
    }

    public void setEstadoMascota(@NotNull(message = "El campo \"Estado de la mascota\" es obligatorio.") EstadoMascota estadoMascota) {
        this.estadoMascota = estadoMascota;
    }

    public @Size(max = 1000, message = "La descripción no debe superar los 1000 caracteres.") @NotBlank(message = "El campo \"Descripción\" es obligatorio.") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@Size(max = 1000, message = "La descripción no debe superar los 1000 caracteres.") @NotBlank(message = "El campo \"Descripción\" es obligatorio.") String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull(message = "El campo \"Tipo de mascota\" es obligatorio.") TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(@NotNull(message = "El campo \"Tipo de mascota\" es obligatorio.") TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public @NotNull Boolean getActivo() {
        return activo;
    }

    public void setActivo(@NotNull Boolean activo) {
        this.activo = activo;
    }
}
