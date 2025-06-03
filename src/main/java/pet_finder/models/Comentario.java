package pet_finder.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "comentarios")
public class Comentario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El comentario tiene que tener texto")
    @Size(max=150, message="Máximo 150 caracteres")
    private String texto;

    @NotNull(message = "La fecha de publicacion es obligatoria")
    private LocalDate fechaPublicacion;


    private Boolean activo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;


    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    public Comentario() {
        this.activo = true;
    }

    public Long getId() {
        return id;
    }


    public Boolean getActivo() {
        return activo;
    }






    public void setActivo(Boolean activo) {
        this.activo = true;
    }


    public Comentario(String texto, LocalDate fechaPublicacion, Publicacion publicacion, Usuario usuario) {
        this.activo = true;
        this.texto = texto;
        this.fechaPublicacion = fechaPublicacion;
        this.publicacion = publicacion;
        this.usuario = usuario;
    }

    public @NotBlank(message = "El comentario tiene que tener texto") @Size(max = 150, message = "Máximo 150 caracteres") String getTexto() {
        return texto;
    }

    public void setTexto(@NotBlank(message = "El comentario tiene que tener texto") @Size(max = 150, message = "Máximo 150 caracteres") String texto) {
        this.texto = texto;
    }

    public @NotNull(message = "La fecha de publicacion es obligatoria") LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(@NotNull(message = "La fecha de publicacion es obligatoria") LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
