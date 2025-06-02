package pet_finder.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;


@Entity
@Table(name = "miembros")
public class Miembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false,length = 50)
    private String nombre;

    @Column(nullable = false,length = 50)
    private String apellido;

    @Email(message = "El correo electrónico no es válido.")
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,length = 15)
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    //Habria que poner la relación con publicaciones. OneToMany. Miembro seria el lado propietario (preguntar mañana)

    public Miembro() {
    }

    public Miembro(String nombre, String apellido, String email,String contrasenia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenia = contrasenia;
        this.rol = Rol.MIEMBRO;         //Ponemos que por defecto sean miembros. La idea seria tener pocos Administradores ya creados.
    }


    public Long getId() {
        return Id;
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


    public @Email String getEmail() {
        return email;
    }
    public void setEmail(@Email String email) {
        this.email = email;
    }


    public String getContrasenia() {
        return contrasenia;
    }
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }


    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

}
