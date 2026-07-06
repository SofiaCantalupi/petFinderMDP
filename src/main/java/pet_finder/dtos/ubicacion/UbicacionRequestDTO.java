package pet_finder.dtos.ubicacion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UbicacionRequestDTO {

        @NotBlank(message = "La direccion no puede estar vacia.")
        @Size(max = 100, message = "La dirección no puede tener más de 100 caracteres.")
        private String direccion;

        @Min(value = 0, message = "La altura no puede ser negativa.")
        private Integer altura;

        @NotNull(message = "La latitud no puede ser null")
        private Double latitud;

        @NotNull(message = "La longitud no puede ser null")
        private Double longitud;

        public UbicacionRequestDTO() {}

        public String getDireccion() {
                return direccion;
        }

        public Integer getAltura() {
                return altura;
        }

        public @NotNull(message = "La latitud no puede ser null") Double getLatitud() {
                return latitud;
        }

        public @NotNull(message = "La longitud no puede ser null") Double getLongitud() {
                return longitud;
        }
}
