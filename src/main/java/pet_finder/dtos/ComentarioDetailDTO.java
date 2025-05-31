package pet_finder.dtos;


import java.time.LocalDate;

//Cuando armás un DTO de detalle no estás obligado a que los atributos coincidan 1:1 con los de la entidad.

public class ComentarioDetailDTO {

    private Long id;

    private String texto;

    private LocalDate fechaPublicacion;

    private Boolean activo;

    private Long idPublicacion;

    private Long idUsuario;

    private String nombreUsuario;



    public ComentarioDetailDTO(Long id, String texto, LocalDate fechaPublicacion, Long idPublicacion, Long idUsuario, String nombreUsuario) {
        this.id = id;
        this.texto = texto;
        this.fechaPublicacion = fechaPublicacion;
        this.idPublicacion = idPublicacion;

        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
    }

    public Long getId() {
        return id;
    }


    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Long getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Long idPublicacion) {
        this.idPublicacion = idPublicacion;
    }


    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}