package pet_finder.dtos.publicacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import pet_finder.dtos.ubicacion.UbicacionRequestDTO;

/**
 * @author Daniel Herrera
 */

public class PublicacionRequestDTO {

        @NotBlank(message = "debe ingresar una descripción con información relevante.")
        private String descripcion;

        @NotNull(message="Debe indicar la mascota.")
        private Long mascotaId;

        @Valid
        private UbicacionRequestDTO ubicacion;

        public PublicacionRequestDTO() {}

        public String getDescripcion() {
                return descripcion;
        }

        public Long getMascotaId() {
                return mascotaId;
        }

        public UbicacionRequestDTO getUbicacion() {
                return ubicacion;
        }

        public void setUbicacion(UbicacionRequestDTO ubicacion) {
                this.ubicacion = ubicacion;
        }
}
