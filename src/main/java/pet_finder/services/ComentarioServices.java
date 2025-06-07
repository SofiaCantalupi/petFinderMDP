package pet_finder.services;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.repositories.ComentarioRepositories;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;

import java.util.List;

@Service
public class ComentarioServices {

    private final ComentarioRepositories comentarioRepository;
    private final PublicacionRepository publicacionRepository;
    private final MiembroRepository miembroRepository;



    public ComentarioServices(ComentarioRepositories comentarioRepository, PublicacionRepository publicacionRepository, MiembroRepository miembroRepository) {
        this.comentarioRepository = comentarioRepository;
        this.publicacionRepository = publicacionRepository;
        this.miembroRepository = miembroRepository;
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

        /*if (dtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        chequear si se agrega
        */

        return comentarioRepository.findByPublicacionIdAndActivoTrue(idPublicacion);
    }





    public void eliminarComentarioPorId(Long id){

        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un comentario con esa id"));

        comentario.setActivo(false);
        comentarioRepository.save(comentario);
    }



}
