package pet_finder.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ciudad;
    private String region; // Estado, provincia o departamento
    private String pais;
    private Double latitud;
    private Double longitud;

    @Column(nullable = false)
    private Boolean activo;

    public Ubicacion() {
        this.activo = true; // Activo default
    }

    public Ubicacion(String ciudad, String region, String pais) {
        this.ciudad = ciudad;
        this.region = region;
        this.pais = pais;
        this.activo = true; // Activo default
    }

    public Ubicacion(String ciudad, String region, String pais, Double latitud, Double longitud) {
        this.ciudad = ciudad;
        this.region = region;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activo = true; // Activo default
    }

    public Long getId() {
        return id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
