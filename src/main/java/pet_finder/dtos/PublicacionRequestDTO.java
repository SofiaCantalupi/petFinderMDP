package pet_finder.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author Daniel Herrera
 */

public class PublicacionRequestDTO {

        @NotBlank(message = "Ingrese alguna información descriptiva de la mascota.")
        private String descripcion;

        @NotNull(message="Debe indicar la mascota")
        private Long mascotaId;

        @Valid
        private UbicacionRequestDTO ubicacion;

        public PublicacionRequestDTO() {}

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

        public UbicacionRequestDTO getUbicacion() {
                return ubicacion;
        }

        public void setUbicacion(UbicacionRequestDTO ubicacion) {
                this.ubicacion = ubicacion;
        }
}
