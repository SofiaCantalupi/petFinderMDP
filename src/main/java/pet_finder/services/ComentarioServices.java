package pet_finder.services;


import org.springframework.stereotype.Service;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.models.Comentario;
import pet_finder.repositories.ComentarioRepositories;

import java.util.List;

@Service
public class ComentarioServices {

    private final ComentarioRepositories comentarioRepository;
    //falta agregar el repositorio de usuario y publicacion.



    public ComentarioServices(ComentarioRepositories comentarioRepository) {
        this.comentarioRepository = comentarioRepository;
    }








    public ComentarioDetailDTO crearComentario(ComentarioRequestDTO requestDTO){

        Publicacion publicacion = publicacionRepository.findById(requestDTO.getIdPublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con esa ID " + requestDTO.getIdPublicacion()));

        Usuario usuario = usuarioRepository.findById(requestDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con esa ID " + requestDTO.getIdUsuario()));
        //cambiar la exception a entityNotFound


        Comentario comentario = new Comentario();

        comentario.setTexto(requestDTO.getTexto());
        comentario.setFechaPublicacion(requestDTO.getFechaPublicacion());
        comentario.setPublicacion(publicacion);
        comentario.setUsuario(usuario);


        //hay que hacer esto porque la base de datos es la genera los ids, si retorno "comentario", no va a tener id
        //porque el metodo save no modifica a "comentario".
        Comentario guardado = comentarioRepository.save(comentario);

        return new ComentarioDetailDTO(
                guardado.getId(),
                guardado.getTexto(),
                guardado.getFechaPublicacion(),
                publicacion.getId(),
                usuario.getId(),
                usuario.getNombre()
        );

    }





    public List<ComentarioDetailDTO> listarComentarios(){

        return comentarioRepository.findAll().stream()
                .map(comentario -> new ComentarioDetailDTO(
                        comentario.getId(),
                        comentario.getTexto(),
                        comentario.getFechaPublicacion(),
                        comentario.getIdPublicacion(),
                        comentario.getIdUsuario
                ))
                .toList();
    }



    public void eliminarComentarioPorID(Long id){
        if (!comentarioRepository.existsById(id)) throw new RuntimeException("No existe un comentario con esa id");
    }

}
