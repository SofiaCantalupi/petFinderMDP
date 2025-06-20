package pet_finder.services;

import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Comentario;
import pet_finder.models.Publicacion;
import pet_finder.repositories.PublicacionRepository;
import org.springframework.stereotype.Service;
import pet_finder.validations.MascotaValidation;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.PublicacionValidation;
import pet_finder.validations.UbicacionValidation;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    private final UbicacionService ubicacionService;
    private final MascotaService mascotaService;
    private final ComentarioServices comentarioService;

    private final PublicacionValidation publicacionValidation;
    private final MiembroValidation miembroValidation;
    private final MascotaValidation mascotaValidation;
    private final UbicacionValidation ubicacionValidation;

    public PublicacionService(PublicacionRepository publicacionRepository,
                              UbicacionService ubicacionService,
                              MascotaService mascotaService,
                              ComentarioServices comentarioService,
                              PublicacionValidation publicacionValidation, MiembroValidation miembroValidation, MascotaValidation mascotaValidation, UbicacionValidation ubicacionValidation) {
        this.publicacionRepository = publicacionRepository;
        this.ubicacionService = ubicacionService;
        this.mascotaService = mascotaService;
        this.comentarioService = comentarioService;
        this.publicacionValidation = publicacionValidation;
        this.miembroValidation = miembroValidation;
        this.mascotaValidation = mascotaValidation;
        this.ubicacionValidation = ubicacionValidation;
    }

    //Nueva publicacion.
    public Publicacion guardar (Publicacion publicacion) {

        // Se valida que la mascota este activa
        mascotaValidation.esActivo(publicacion.getMascota().getEsActivo());

        // Se valida que la mascota no pertenezca a otra publicacion
        publicacionValidation.mascotaYaAsignada(publicacion.getMascota().getId());

        // Se valida que la ubicacion pueda ser geocodificada
        ubicacionValidation.validarGeocodificacion(publicacion.getUbicacion());

        return publicacionRepository.save(publicacion);
    }

    public Publicacion guardarModificada (Publicacion publicacion) {

        // Se valida que la ubicacion pueda ser geocodificada
        ubicacionValidation.validarGeocodificacion(publicacion.getUbicacion());

        return publicacionRepository.save(publicacion);
    }

    public Publicacion obtenerPorId (Long id) {

        // Valida si existe la Publicacion con ese id, si existe la retorna
        Publicacion existente = publicacionValidation.existePorId(id);

        // Valida si la Publicacion esta activa
        publicacionValidation.esActivo(existente.getActivo());
        return existente;
    }

    // LISTAR TODAS LAS PUBLICACIONES
    public List<Publicacion> listarTodas () {
        return publicacionRepository.findAll();
    }

    // LISTAR LAS PUBLICACIONES ACTIVAS
    public List<Publicacion> listarActivas () {
        return publicacionRepository.findAllByActivoTrue();
    }

    //Eliminar publicación como administrador
    public void eliminar(Long id) {

        // Valida si existe la Publicacion por id
        Publicacion p = publicacionValidation.existePorId(id);

        // Valida si la Publicacion ya estaba activa
        publicacionValidation.esActivo(p.getActivo());

        // Eliminar mascota por service
        mascotaService.eliminar(p.getMascota().getId());

        // Eliminar ubicacion por service
        ubicacionService.eliminar(p.getUbicacion().getId());

        // Eliminar comentarios por service
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(p.getId());
        for(Comentario c: comentarios) {
            comentarioService.eliminarComentarioPorId(c.getId());
        }

        // BAJA LOGICA
        p.setActivo(false);

        // SE ACTUALIZA REALIZANDO SU BAJA LOGICA
        publicacionRepository.save(p);
    }

    // FILTRAR POR TipoMascota
    public List<Publicacion> filtrarPorTipoMascota(String tipoString){

        // El controller recibe un String, por lo tanto debe convertirse a un dato tipo Enum (TipoMascota)
        // Se valida que el string sea valido ("gato" o "perro") y se convierte a su respectivo Enum (TipoMascota)
        TipoMascota tipoEnum = publicacionValidation.validarYConvertirTipoMascota(tipoString);

        // Se buscan las publicaciones cuyas mascotas son del tipo ingresado por parametro, y se filtran las publicaciones activas
        return publicacionRepository.findAllByMascotaTipoMascota(tipoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();
    }

    // FILTRAR POR EstadoMascota
    public List<Publicacion> filtrarPorEstadoMascota(String estadoString){

        // Se valida que el string sea valido ("perdido" o "encontrado") y se convierte a su respectivo Enum (EstadoMascota)
        EstadoMascota estadoEnum = publicacionValidation.validarYConvertirEstadoMascota(estadoString);

        // El metodo encuentra todas las publicaciones con ese estado y filtra las publicaciones activas.
        return publicacionRepository.findAllByMascotaEstadoMascota(estadoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();
    }

    //Eliminar publicación como miembro.
    public void eliminarPublicacionPropia(Long idPublicacion,Long idMiembroLogeado){

       Publicacion publicacion = publicacionValidation.existePorId(idPublicacion);

       //Si el ID del miembro autenticado y el del miembro no coinciden
        //se lanza una excepción ya que se estaria tratando de borrar una publicación
        //que no es del usuario autenticado.
        miembroValidation.estaLogeado(publicacion.getIdMiembro(),idMiembroLogeado);

        this.eliminar(idPublicacion);
    }
}
