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




    public Comentario crearComentario(Comentario comentario){

        Publicacion publicacion = publicacionRepository.findById(comentario.getPublicacion().getId())
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con esa ID " + comentario.getPublicacion().getId()));

        Miembro miembro = miembroRepository.findById(comentario.getMiembro().getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con esa ID " + comentario.getMiembro().getId()));


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







     // lo dejo por las dudas, despues se elimina, pero esto es para trabajar sin la interfaz Mapper
     /* public ComentarioDetailDTO crearComentario(ComentarioRequestDTO requestDTO){

        Publicacion publicacion = publicacionRepository.findById(requestDTO.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con esa ID " + requestDTO.getIdPublicacion()));

        Miembro miembro = miembroRepository.findById(requestDTO.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con esa ID " + requestDTO.getIdUsuario()));



        Comentario comentario = new Comentario();

        comentario.setTexto(requestDTO.getTexto());
        comentario.setFechaPublicacion(requestDTO.getFechaPublicacion());
        comentario.setPublicacion(publicacion);
        comentario.setMiembro(miembro);



        Comentario guardado = comentarioRepository.save(comentario);

        return new ComentarioDetailDTO(guardado);



        public List<ComentarioDetailDTO> listarComentarios(){

        return comentarioRepository.findAll().stream()
                .map(comentario -> new ComentarioDetailDTO(comentario))
                .toList();
    }

    }*/

}
