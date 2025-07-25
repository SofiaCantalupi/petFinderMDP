package pet_finder.models;

import jakarta.persistence.*;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nombre;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMascota estadoMascota;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMascota tipoMascota;

    @Column(nullable = false)
    private Boolean esActivo;

    @Column(nullable = false)
    private Long miembroId;

    private String fotoUrl;

    // Constructor vacio
    public Mascota() {
        this.esActivo = true;
    }

    // Setters & Getters

    public Long getId() {
        return id;
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

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public Long getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(Long miembroId) {
        this.miembroId = miembroId;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }
}
