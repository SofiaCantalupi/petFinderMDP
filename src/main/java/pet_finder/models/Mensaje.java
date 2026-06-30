package pet_finder.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String texto;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(nullable = false)
    private Boolean leido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emisor", nullable = false)
    private Miembro emisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_receptor", nullable = false)
    private Miembro receptor;

    public Mensaje() {
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
    }

    public Mensaje(String texto, Miembro emisor, Miembro receptor) {
        this.texto = texto;
        this.fechaEnvio = LocalDateTime.now();
        this.leido = false;
        this.emisor = emisor;
        this.receptor = receptor;
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

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public Miembro getEmisor() {
        return emisor;
    }

    public void setEmisor(Miembro emisor) {
        this.emisor = emisor;
    }

    public Miembro getReceptor() {
        return receptor;
    }

    public void setReceptor(Miembro receptor) {
        this.receptor = receptor;
    }
}
