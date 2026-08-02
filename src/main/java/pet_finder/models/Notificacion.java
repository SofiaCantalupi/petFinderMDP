package pet_finder.models;

import jakarta.persistence.*;
import pet_finder.enums.TipoNotificacion;

import java.time.LocalDate;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receptor_id", nullable = false)
    private Miembro receptor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id", nullable = false)
    private Miembro emisor;

    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    //Se refiere a que entidad es la que esta relacionada con esta publicacion. Puede ser un comentario, publicacion o solicitud de adopcion.
    @Column(nullable = false)
    private Long entidadReferenciaId;

    @Column(nullable = false)
    private boolean leida;

    private LocalDate fecha;

    @Column(nullable = false)
    private boolean activa;

    public Notificacion(Miembro receptor,Miembro emisor,TipoNotificacion tipo,Long entidadReferenciaId) {
        this.receptor = receptor;
        this.emisor = emisor;
        this.tipo = tipo;
        this.entidadReferenciaId = entidadReferenciaId;
        this.leida = false;
        this.fecha = LocalDate.now();
        this.activa = true;
    }

    public Notificacion() {
        this.leida = false;
        this.fecha = LocalDate.now();
        this.activa = true;
    }

    public Long getId() {
        return id;
    }

    public Miembro getReceptor() {
        return receptor;
    }
    public void setReceptor(Miembro receptor) {
        this.receptor = receptor;
    }


    public Miembro getEmisor() {
        return emisor;
    }
    public void setEmisor(Miembro emisor) {
        this.emisor = emisor;
    }


    public TipoNotificacion getTipo() {
        return tipo;
    }
    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public Long getEntidadReferenciaId() {
        return entidadReferenciaId;
    }
    public void setEntidadReferenciaId(Long entidadReferenciaId) {
        this.entidadReferenciaId = entidadReferenciaId;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


    public boolean isLeida() {
        return leida;
    }
    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public boolean isActiva() {
        return activa;
    }
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
