package com.UTN.pet_finder_mdp.dtos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PublicacionRequestDTO {

    @NotBlank(message = "Ingrese alguna información descriptiva de la mascota.")
    private String descripcion;

    @NotNull(message="Debe indicar la mascota")
    private Long mascotaId;

    @NotNull(message="Debe indicar al miembro relacionado")
    private Long miembroId;

    @NotNull(message="Debe indicar la ubicacion")
    private Long ubicacionId;

    public PublicacionRequestDTO() {
    }

    public PublicacionRequestDTO(String descripcion, Long mascotaId, Long miembroId, Long ubicacionId) {
        this.descripcion = descripcion;
        this.mascotaId = mascotaId;
        this.miembroId = miembroId;
        this.ubicacionId = ubicacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
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
}
