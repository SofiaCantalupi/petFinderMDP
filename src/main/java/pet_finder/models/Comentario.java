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

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private LocalDate fechaPublicacion;

    private Boolean activo;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private Publicacion publicacion;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_miembro", nullable = false)
    private Miembro miembro;


    public Comentario() {
        this.activo = true;
        this.fechaPublicacion = LocalDate.now();
    }

    public Comentario(String texto, Publicacion publicacion, Miembro miembro) {
        this.activo = true;
        this.texto = texto;
        this.fechaPublicacion = LocalDate.now();
        this.publicacion = publicacion;
        this.miembro = miembro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }
}
