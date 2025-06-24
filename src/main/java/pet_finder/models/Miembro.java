package pet_finder.models;

import jakarta.persistence.*;
import pet_finder.enums.RolUsuario;

@Entity
@Table(name = "miembros")
public class Miembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String nombre;

    @Column(nullable = false,length = 50)
    private String apellido;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,length = 100)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rolUsuario;

    @Column(nullable = false)
    private boolean activo;

    public Miembro() {
    }

    public Miembro(String nombre, String apellido, String email,String contrasenia,boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }


    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }


    public RolUsuario getRol() {
        return rolUsuario;
    }
    public void setRol(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
