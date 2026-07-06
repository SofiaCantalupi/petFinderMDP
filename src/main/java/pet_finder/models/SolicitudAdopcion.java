package pet_finder.models;

import jakarta.persistence.*;
import pet_finder.enums.EstadoSolicitud;
import pet_finder.enums.MotivoRechazo;
import pet_finder.enums.TipoHogar;
import pet_finder.enums.TipoMascotasEnHogar;

import java.time.LocalDateTime;

@Entity
@Table(name="solicitudes")
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "miembro_solicitante_id", nullable = false)
    private Miembro miembroSolicitante;

    private String celular;

    @Enumerated(EnumType.STRING)
    private TipoHogar tipoHogar;

    private boolean hayMascotaEnHogar;

    private boolean tienePatio;

    private boolean aceptaCondiciones;

    @Column(columnDefinition = "TEXT")
    private String motivoAdopcion;

    @Enumerated(EnumType.STRING)
    private TipoMascotasEnHogar tipoMascotasEnHogar;

    private LocalDateTime fechaResolucion; // nullable

    private String comentarioResolucion;   // nullable

    @Enumerated(EnumType.STRING)
    private MotivoRechazo motivoRechazo; //nullable

    public SolicitudAdopcion() {
        this.fecha = LocalDateTime.now();
        this.estado = EstadoSolicitud.PENDIENTE;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MotivoRechazo getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(MotivoRechazo motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public Miembro getMiembroSolicitante() {
        return miembroSolicitante;
    }

    public void setMiembroSolicitante(Miembro miembroSolicitante) {
        this.miembroSolicitante = miembroSolicitante;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public TipoHogar getTipoHogar() {
        return tipoHogar;
    }

    public void setTipoHogar(TipoHogar tipoHogar) {
        this.tipoHogar = tipoHogar;
    }

    public boolean isHayMascotaEnHogar() {
        return hayMascotaEnHogar;
    }

    public void setHayMascotaEnHogar(boolean hayMascotaEnHogar) {
        this.hayMascotaEnHogar = hayMascotaEnHogar;
    }

    public boolean isTienePatio() {
        return tienePatio;
    }

    public void setTienePatio(boolean tienePatio) {
        this.tienePatio = tienePatio;
    }

    public boolean isAceptaCondiciones() {
        return aceptaCondiciones;
    }

    public void setAceptaCondiciones(boolean aceptaCondiciones) {
        this.aceptaCondiciones = aceptaCondiciones;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }

    public void setMotivoAdopcion(String motivoAdopcion) {
        this.motivoAdopcion = motivoAdopcion;
    }

    public TipoMascotasEnHogar getTipoMascotasEnHogar() {
        return tipoMascotasEnHogar;
    }

    public void setTipoMascotasEnHogar(TipoMascotasEnHogar tipoMascotasEnHogar) {
        this.tipoMascotasEnHogar = tipoMascotasEnHogar;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    public String getComentarioResolucion() {
        return comentarioResolucion;
    }

    public void setComentarioResolucion(String comentarioResolucion) {
        this.comentarioResolucion = comentarioResolucion;
    }
}
