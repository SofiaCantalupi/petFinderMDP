package pet_finder.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.repositories.ComentarioRepositories;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;
import pet_finder.exceptions.OperacionNoPermitidaException;
import pet_finder.validations.ComentarioValidation;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.PublicacionValidation;

import java.util.List;

@Service
public class ComentarioServices {

    private final ComentarioRepositories comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final ComentarioValidation comentarioValidation;
    private final MiembroValidation miembroValidation;
    private final PublicacionValidation publicacionValidation;


    public ComentarioServices(ComentarioRepositories comentarioRepository, PublicacionRepository publicacionRepository, ComentarioValidation comentarioValidation, MiembroValidation miembroValidation, PublicacionValidation publicacionValidation) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.comentarioValidation = comentarioValidation;
        this.miembroValidation = miembroValidation;
        this.publicacionValidation = publicacionValidation;
    }

    public Comentario crearComentario(Comentario comentario, Long idPublicacion, Long idMiembro){

        Publicacion publicacion = publicacionValidation.existePorId(idPublicacion);

        Miembro miembro = miembroValidation.validarExistenciaPorId(idMiembro);

        comentario.setPublicacion(publicacion);
        comentario.setMiembro(miembro);

        // Agregaria el comentario en la lista de la Publicacion
        publicacion.agregarComentario(comentario);
        publicacionRepository.save(publicacion);

        return comentarioRepository.save(comentario);
    }


    public List<Comentario> listarPorPublicacion(Long idPublicacion) {
        Publicacion p = publicacionValidation.existePorId(idPublicacion);
        publicacionValidation.esActivo(p.getActivo());
        return comentarioRepository.findByPublicacionIdAndActivoTrue(idPublicacion);
    }


    public void eliminarComentarioPorId(Long id){

        Comentario comentario = comentarioValidation.existePorId(id);
        // Se valida que el comentario no haya sido eliminado anteriormente
        comentarioValidation.esActivo(comentario.getActivo());

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }

    public void eliminarComentarioPropio(Long idComentario, Long idMiembroLogeado) {

        Comentario comentario = comentarioValidation.existePorId(idComentario);

        // Se valida que el comentario no haya sido eliminado anteriormente
        comentarioValidation.esActivo(comentario.getActivo());

        miembroValidation.estaLogeado(comentario.getMiembro().getId(),idMiembroLogeado);

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }
}