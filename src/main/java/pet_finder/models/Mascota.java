package pet_finder.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;

@Entity
@Table(name = "mascotas")
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nombre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMascota estadoMascota;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMascota tipoMascota;

    @Column(length = 1000, nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;

    // Constructor vacio
    public Mascota() {
        this.activo = true;
    }

    // Setters & Getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoMascota getEstadoMascota() {
        return estadoMascota;
    }

    public void setEstadoMascota(EstadoMascota estadoMascota) {
        this.estadoMascota = estadoMascota;
    }

    public TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public @Size(max = 1000, message = "La descripción no debe superar los 1000 caracteres.") @NotBlank(message = "El campo \"Descripción\" es obligatorio.") String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(@Size(max = 1000, message = "La descripción no debe superar los 1000 caracteres.") @NotBlank(message = "El campo \"Descripción\" es obligatorio.") String descripcion) {
        this.descripcion = descripcion;
    }

    public @NotNull Boolean getActivo() {
        return activo;
    }

    public void setActivo(@NotNull Boolean activo) {
        this.activo = activo;
    }
}
