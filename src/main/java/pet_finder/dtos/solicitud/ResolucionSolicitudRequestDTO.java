package pet_finder.dtos.solicitud;

import jakarta.validation.constraints.NotBlank;

public class ResolucionSolicitudRequestDTO {
    @NotBlank
    private String estado;

    private String comentarioResolucion;

    public ResolucionSolicitudRequestDTO(){}

    public @NotBlank String getEstado() {
        return estado;
    }

    public void setEstado(@NotBlank String estado) {
        this.estado = estado;
    }

    public String getComentarioResolucion() {
        return comentarioResolucion;
    }

    public void setComentarioResolucion(String comentarioResolucion) {
        this.comentarioResolucion = comentarioResolucion;
    }
}
