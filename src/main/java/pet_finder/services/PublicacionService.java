package pet_finder.services;

import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.exceptions.OperacionNoPermitidaException;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.validations.PublicacionValidation;

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
    private final MiembroRepository miembroRepository;

    private final PublicacionValidation publicacionValidation;

    public PublicacionService(PublicacionRepository publicacionRepository,
                              UbicacionService ubicacionService,
                              MascotaService mascotaService,
                              ComentarioServices comentarioService, MiembroRepository miembroRepository,
                              PublicacionValidation publicacionValidation) {
        this.publicacionRepository = publicacionRepository;
        this.ubicacionService = ubicacionService;
        this.mascotaService = mascotaService;
        this.comentarioService = comentarioService;
        this.miembroRepository = miembroRepository;
        this.publicacionValidation = publicacionValidation;
    }

    // NUEVA PUBLICACION
    public Publicacion guardar (Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    // LISTAR POR ID
    public Publicacion obtenerPorId (Long id) {
        // Valida si existe la Publicacion con ese id, si existe la retorna
        Publicacion existente = publicacionValidation.existePorId(id);

        // Valida si la Publicacion esta activa
        publicacionValidation.esInactivo(existente);

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

    // MODIFICAR SE REALIZA MEDIANTE MAPPER

    // BAJA LOGICA
    public void eliminar(Long id) {
        // Valida si existe la Publicacion por id
        Publicacion p = publicacionValidation.existePorId(id);

        // Valida si la Publicacion ya estaba activa
        publicacionValidation.esInactivo(p);

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

    public void eliminarPublicacionPropia(Long idPublicacion, String emailMiembro){

       Publicacion publicacion = publicacionValidation.existePorId(idPublicacion);

        Miembro miembro = miembroRepository.findByEmail(emailMiembro)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un usuario con el email: " + emailMiembro));

        if(!publicacion.getIdMiembro().equals(miembro.getId())){
            throw new OperacionNoPermitidaException("No puedes eliminar una publicación que no es tuya.");
        }

        this.eliminar(idPublicacion);
    }
}
