package pet_finder.dtos.solicitud;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pet_finder.enums.TipoHogar;
import pet_finder.enums.TipoMascotasEnHogar;

public class SolicitudAdopcionRequestDTO {
    private Long idPublicacion;

    @NotBlank(message = "Debe ingresar un celular.")
    private String celular;

    @NotNull(message = "Debe indicar un tipo de hogar")
    private TipoHogar tipoHogar;

    @NotNull(message = "Debe indicar si hay mascotas en su hogar.")
    private Boolean hayMascotaEnHogar;

    private TipoMascotasEnHogar tipoMascotasEnHogar;

    @NotNull(message = "Debe indicar si tiene patio.")
    private Boolean tienePatio;

    @AssertTrue(message = "Debe aceptar las condiciones establecidas para la adopción.")
    private boolean aceptaCondiciones;

    private String motivoAdopcion;

    public SolicitudAdopcionRequestDTO(){}

    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Long idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public TipoHogar getTipoHogar() {
        return tipoHogar;
    }

    public void setTipoHogar(TipoHogar tipoHogar) {
        this.tipoHogar = tipoHogar;
    }

    public Boolean getHayMascotaEnHogar() {
        return hayMascotaEnHogar;
    }

    public void setHayMascotaEnHogar(Boolean hayMascotaEnHogar) {
        this.hayMascotaEnHogar = hayMascotaEnHogar;
    }

    public TipoMascotasEnHogar getTipoMascotasEnHogar() {
        return tipoMascotasEnHogar;
    }

    public void setTipoMascotasEnHogar(TipoMascotasEnHogar tipoMascotasEnHogar) {
        this.tipoMascotasEnHogar = tipoMascotasEnHogar;
    }

    public Boolean getTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(Boolean tienePatio) {
        this.tienePatio = tienePatio;
    }

    public boolean isAceptaCondiciones() {
        return aceptaCondiciones;
    }

    public void setAceptaCondiciones(boolean aceptaCondiciones) {
        this.aceptaCondiciones = aceptaCondiciones;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }

    public void setMotivoAdopcion(String motivoAdopcion) {
        this.motivoAdopcion = motivoAdopcion;
    }

    @AssertTrue(message = "El tipo de mascotas en el hogar es inconsistente con si hay mascotas en el hogar.")
    public boolean isTipoMascotasEnHogarConsistente() {
        if (hayMascotaEnHogar == null) {
            return true; // deja que @NotNull sea quien reporte el error, evita doble mensaje
        }
        if (!hayMascotaEnHogar) {
            return tipoMascotasEnHogar == null;
        }
        return tipoMascotasEnHogar != null;
    }
}