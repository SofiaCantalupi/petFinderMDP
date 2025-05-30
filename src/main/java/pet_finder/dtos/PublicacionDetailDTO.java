package com.UTN.pet_finder_mdp.dtos;

import java.time.LocalDate;

public record PublicacionDetailDTO {

    private Long id;
    private String descripcion;
    private LocalDate fecha;
    private Boolean activo;

    private Long mascotaId;
    private String nombreMascota;

    private Long miembroId;
    private String nombreMiembro;

    private Long ubicacionId;
    private String direccion;

    public PublicacionDetailDTO() {
    }

    public PublicacionDetailDTO(Long id, String descripcion, LocalDate fecha, Boolean activo, Long mascotaId, String nombreMascota, Long miembroId, String nombreMiembro, Long ubicacionId, String direccion) {
        this.id = id;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.activo = activo;
        this.mascotaId = mascotaId;
        this.nombreMascota = nombreMascota;
        this.miembroId = miembroId;
        this.nombreMiembro = nombreMiembro;
        this.ubicacionId = ubicacionId;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getNombreMiembro() {
        return nombreMiembro;
    }

    public void setNombreMiembro(String nombreMiembro) {
        this.nombreMiembro = nombreMiembro;
    }

    public Long getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(Long miembroId) {
        this.miembroId = miembroId;
    }

    public Long getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Long ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
