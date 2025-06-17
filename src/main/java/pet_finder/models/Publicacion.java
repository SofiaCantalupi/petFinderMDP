package pet_finder.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Herrera
 */

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fecha;

    @OneToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    private Long idMiembro;

    // fetch me permite recibir Ubicacion a la vez que obtengo PublicacionById
    // cascade me permite crear o modificar la Ubicacion mediante la Publicacion
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column(nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "publicacion")
    private List<Comentario> comentarios = new ArrayList<>();

    public Publicacion() {
        this.activo = true; // Activo default
        this.fecha = LocalDate.now(); // Fecha de hoy
    }

    public Publicacion(String descripcion, Mascota mascota, Ubicacion ubicacion, List<Comentario> comentarios) {
        this.descripcion = descripcion;
        this.mascota = mascota;
        this.ubicacion = ubicacion;

        this.activo = true; // Activo default
        this.fecha = LocalDate.now(); // Fecha de hoy
        this.comentarios = comentarios;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Long getIdMiembro() {
        return idMiembro;
    }

    public void setIdMiembro(Long idMiembro) {
        this.idMiembro = idMiembro;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    //Metodo helper para guardar un comentario en la lista de comentarios
    public void agregarComentario(Comentario comentario) {
    this.comentarios.add(comentario);
    }
}
