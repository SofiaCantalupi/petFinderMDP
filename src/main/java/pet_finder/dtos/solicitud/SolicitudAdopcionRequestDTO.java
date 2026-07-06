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

    private boolean hayMascotaEnHogar;

    private TipoMascotasEnHogar tipoMascotasEnHogar;

    private boolean tienePatio;

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

    public @NotBlank(message = "Debe ingresar un celular.") String getCelular() {
        return celular;
    }

    public void setCelular(@NotBlank(message = "Debe ingresar un celular.") String celular) {
        this.celular = celular;
    }

    public @NotNull(message = "Debe indicar un tipo de hogar") TipoHogar getTipoHogar() {
        return tipoHogar;
    }

    public void setTipoHogar(@NotNull(message = "Debe indicar un tipo de hogar") TipoHogar tipoHogar) {
        this.tipoHogar = tipoHogar;
    }

    @NotNull(message = "Debe indicar si viven mascotas en su hogar.")
    public boolean isHayMascotaEnHogar() {
        return hayMascotaEnHogar;
    }

    public void setHayMascotaEnHogar(@NotNull(message = "Debe indicar si viven mascotas en su hogar.") boolean hayMascotaEnHogar) {
        this.hayMascotaEnHogar = hayMascotaEnHogar;
    }

    public @NotNull(message = "Debe indicar qué tipo de mascota vive en su hogar.") TipoMascotasEnHogar getTipoMascotasEnHogar() {
        return tipoMascotasEnHogar;
    }

    public void setTipoMascotasEnHogar(@NotNull(message = "Debe indicar qué tipo de mascota vive en su hogar.") TipoMascotasEnHogar tipoMascotasEnHogar) {
        this.tipoMascotasEnHogar = tipoMascotasEnHogar;
    }

    @NotNull(message = "Debe indicar si tiene patio.")
    public boolean isTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(@NotNull(message = "Debe indicar si tiene patio.") boolean tienePatio) {
        this.tienePatio = tienePatio;
    }

    @NotNull(message = "Debe indicar si acepta o no las condiciones establecidas para la adopción.")
    public boolean isAceptaCondiciones() {
        return aceptaCondiciones;
    }

    public void setAceptaCondiciones(@NotNull(message = "Debe indicar si acepta o no las condiciones establecidas para la adopción.") boolean aceptaCondiciones) {
        this.aceptaCondiciones = aceptaCondiciones;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }

    public void setMotivoAdopcion(String motivoAdopcion) {
        this.motivoAdopcion = motivoAdopcion;
    }
}
