package com.UTN.pet_finder_mdp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ingrese alguna información descriptiva de la mascota.")
    @Column()
    private String descripcion;

    private LocalDate fecha;

    @NotNull
    private Boolean activo;

    @OneToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;
    @ManyToOne
    @JoinColumn(name = "miembro_id")
    private Miembro miembro;
    @OneToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    public Publicacion() {
        this.activo = true; // Activo default
        this.fecha = LocalDate.now(); // Fecha de hoy
    }

    public Publicacion(String descripcion, Mascota mascota, Miembro miembro, Ubicacion ubicacion) {
        this.descripcion = descripcion;
        this.mascota = mascota;
        this.miembro = miembro;
        this.ubicacion = ubicacion;

        this.activo = true; // Activo default
        this.fecha = LocalDate.now(); // Fecha de hoy
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
