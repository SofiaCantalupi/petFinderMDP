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

import java.util.List;

@Service
public class ComentarioServices {

    private final ComentarioRepositories comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final MiembroRepository miembroRepository;
    private final ComentarioValidation comentarioValidation;
    //agregar los validator de miembro y publicacion una vez arreglados.

    public ComentarioServices(ComentarioRepositories comentarioRepository, PublicacionRepository publicacionRepository, MiembroRepository miembroRepository, ComentarioValidation comentarioValidation) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.miembroRepository = miembroRepository;
        this.comentarioValidation = comentarioValidation;
    }

    public Comentario crearComentario(Comentario comentario, Long idPublicacion, Long idMiembro){

        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con esa ID " + idPublicacion));

        Miembro miembro = miembroRepository.findById(idMiembro)
                .orElseThrow(() -> new EntityNotFoundException("Miembro no encontrado con esa ID " + idMiembro));


        comentario.setPublicacion(publicacion);
        comentario.setMiembro(miembro);

        return comentarioRepository.save(comentario);
    }


    public List<Comentario> listarPorPublicacion(Long idPublicacion) {
        return comentarioRepository.findByPublicacionIdAndActivoTrue(idPublicacion);
    }



    public void eliminarComentarioPorId(Long id){

        Comentario comentario = comentarioValidation.existePorId(id);

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }

    public void eliminarComentarioPropio(Long idComentario, String emailMiembro) {

        Comentario comentario = comentarioValidation.existePorId(idComentario);

        Miembro miembro = miembroRepository.findByEmail(emailMiembro)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un usuario con el email: " + emailMiembro));

        // valida que el comentario sea propio
        if (!comentario.getMiembro().getId().equals(miembro.getId())) {
            throw new OperacionNoPermitidaException("No puedes eliminar un comentario que no es tuyo.");
        }

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }
}