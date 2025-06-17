package pet_finder.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author Daniel Herrera
 */
public class UbicacionRequestDTO {

        @NotBlank(message = "La direccion no puede estar vacia.")
        @Size(max = 100, message = "La dirección no puede tener más de 100 caracteres.")
        private String direccion;

        @Min(value = 0, message = "La altura no puede ser negativa.")
        private Integer altura;

        @NotBlank(message = "La ciudad no puede estar vacia.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        private String ciudad;

        @NotBlank(message = "La region no puede estar vacia.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        private String region; // Estado, provincia o departamento

        @NotBlank(message = "El pais no puede estar vacio.")
        @Size(max = 50, message = "La dirección no puede tener más de 50 caracteres.")
        private String pais;

        public UbicacionRequestDTO() {}

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
}
