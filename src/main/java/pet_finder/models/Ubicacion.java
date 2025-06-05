package pet_finder.models;

import jakarta.persistence.*;

/**
 * @author Daniel Herrera
 */

@Entity
@Table(name = "ubicaciones")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direccion;
    private Integer altura;

    private String ciudad;
    private String region; // Estado, provincia o departamento
    private String pais;

    private Double latitud;
    private Double longitud;

    @Column(nullable = false)
    private Boolean activo;

    public Ubicacion() {
        this.activo = true; // Activo por default
    }

    public Ubicacion(String direccion, Integer altura, String ciudad, String region, String pais) {
        this.direccion = direccion;
        this.altura = altura;
        this.ciudad = ciudad;
        this.region = region;
        this.pais = pais;
        this.activo = true; // Activo por default
    }

    public Ubicacion(String direccion, Integer altura, String ciudad, String region, String pais, Double latitud, Double longitud) {
        this.direccion = direccion;
        this.altura = altura;
        this.ciudad = ciudad;
        this.region = region;
        this.pais = pais;
        this.latitud = latitud;
        this.longitud = longitud;
        this.activo = true; // Activo por default
    }

    public Long getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getAltura() {
        return altura;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
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
