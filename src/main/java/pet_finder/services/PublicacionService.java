package pet_finder.services;

import pet_finder.models.Comentario;
import pet_finder.models.Publicacion;
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

    private final PublicacionValidation publicacionValidation;

    public PublicacionService(PublicacionRepository publicacionRepository,
                              UbicacionService ubicacionService,
                              MascotaService mascotaService,
                              ComentarioServices comentarioService,
                              PublicacionValidation publicacionValidation) {
        this.publicacionRepository = publicacionRepository;
        this.ubicacionService = ubicacionService;
        this.mascotaService = mascotaService;
        this.comentarioService = comentarioService;
        this.publicacionValidation = publicacionValidation;
    }

    // NUEVA PUBLICACION
    public Publicacion guardar (Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    // LISTAR POR ID
    public Publicacion obtenerPorId (Long id) {
        // Valida si existe la Publicacion con ese id
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        // Valida si la Publicacion esta activa
        publicacionValidation.esInactivo(p);

        return p;
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
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

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

}
