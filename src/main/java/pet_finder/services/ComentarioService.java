package pet_finder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.dtos.comentario.ComentarioRequestDTO;
import pet_finder.enums.TipoNotificacion;
import pet_finder.mappers.ComentarioMapper;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.repositories.ComentarioRepository;
import pet_finder.validations.ComentarioValidation;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.PublicacionValidation;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;

    private final ComentarioValidation comentarioValidation;
    private final MiembroValidation miembroValidation;
    private final PublicacionValidation publicacionValidation;

    private final ComentarioMapper comentarioMapper;

    private final NotificacionService notificacionService;

    public ComentarioService(ComentarioRepository comentarioRepository,
            ComentarioValidation comentarioValidation, MiembroValidation miembroValidation,
            PublicacionValidation publicacionValidation, ComentarioMapper comentarioMapper,
            NotificacionService notificacionService) {
        this.comentarioRepository = comentarioRepository;
        this.comentarioValidation = comentarioValidation;
        this.miembroValidation = miembroValidation;
        this.publicacionValidation = publicacionValidation;
        this.comentarioMapper = comentarioMapper;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public ComentarioDetailDTO crearComentario(ComentarioRequestDTO request, Long idMiembro) {

        Comentario comentario = comentarioMapper.aEntidad(request);

        Publicacion publicacion = publicacionValidation.existePorId(request.getIdPublicacion());
        publicacionValidation.esActivo(publicacion.getActivo());

        Miembro miembro = miembroValidation.validarExistenciaPorId(idMiembro);

        comentario.setPublicacion(publicacion);
        comentario.setMiembro(miembro);

        // Agregaria el comentario en la lista de la Publicacion.
        // No hace falta guardar la publicacion: el @OneToMany no tiene cascade,
        // el comentario se persiste con su propio save.
        publicacion.agregarComentario(comentario);

        Comentario creado = comentarioRepository.save(comentario);

        // Valido que el que comento la publicacion no sea el dueño para evitar
        // notificacion sin sentido.
        if (!publicacion.getMiembro().getId().equals(miembro.getId())) {

            notificacionService.generarNotificacion(
                    publicacion.getMiembro().getId(),
                    miembro.getId(),
                    TipoNotificacion.NUEVO_COMENTARIO,
                    creado.getId());
        }

        return comentarioMapper.aDetail(creado);
    }

    // Muestra los comentarios de una publicación por su ID.
    @Transactional(readOnly = true)
    public List<Comentario> listarPorPublicacion(Long idPublicacion) {

        Publicacion p = publicacionValidation.existePorId(idPublicacion);
        publicacionValidation.esActivo(p.getActivo());

        return comentarioRepository.findByPublicacionIdAndActivoTrue(idPublicacion);
    }

    @Transactional(readOnly = true)
    public List<ComentarioDetailDTO> listarDetallesPorPublicacion(Long idPublicacion) {
        return comentarioMapper.deEntidadesAdetails(listarPorPublicacion(idPublicacion));
    }

    @Transactional(readOnly = true)
    public ComentarioDetailDTO obtenerDetallePorId(Long id) {
        Comentario comentario = comentarioValidation.existePorId(id);
        return comentarioMapper.aDetail(comentario);
    }

    @Transactional
    public void eliminarComentarioPorId(Long id) {

        Comentario comentario = comentarioValidation.existePorId(id);
        // Se valida que el comentario no haya sido eliminado anteriormente
        comentarioValidation.esActivo(comentario.getActivo());

        // Borro las notificaciones asociadas al comentario
        notificacionService.eliminarNotificacionesComentario(comentario.getId());

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }

    @Transactional
    public void eliminarComentarioPropio(Long idComentario, Long idMiembroLogeado) {

        Comentario comentario = comentarioValidation.existePorId(idComentario);

        // Se valida que el comentario no haya sido eliminado anteriormente
        comentarioValidation.esActivo(comentario.getActivo());

        // Valida que el usuario autenticado coincida con el autor del comentario que se
        // va a borrar.
        miembroValidation.estaLogeado(comentario.getMiembro().getId(), idMiembroLogeado);

        // Borro las notificaciones asociadas al comentario
        notificacionService.eliminarNotificacionesComentario(comentario.getId());

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }

    // Uso exclusivo de MiembroService al dar de baja una cuenta (propia o por un administrador):
    // da de baja todos los comentarios que el miembro escribio, esten en la publicacion que esten.
    @Transactional
    public void eliminarComentariosPorMiembro(Long idMiembro) {

        List<Comentario> comentarios = comentarioRepository.findByMiembroIdAndActivoTrue(idMiembro);

        comentarios.forEach(comentario -> {
            // Borro las notificaciones asociadas al comentario
            notificacionService.eliminarNotificacionesComentario(comentario.getId());

            comentario.setActivo(false);
        });

        comentarioRepository.saveAll(comentarios);
    }
}