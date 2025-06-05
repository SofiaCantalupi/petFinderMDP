package pet_finder.dtos;


import pet_finder.models.Comentario;

import java.time.LocalDate;


public record ComentarioDetailDTO (Long id, String texto, LocalDate fechaPublicacion, Boolean activo, Long idPublicacion, Long idMiembro, String nombreUsuario){


    public ComentarioDetailDTO(Comentario comentario){
        this(
          comentario.getId(),
          comentario.getTexto(),
          comentario.getFechaPublicacion(),
          comentario.getActivo(),
          comentario.getPublicacion().getId(),
          comentario.getMiembro().getId(),
          comentario.getMiembro().getNombre()
        );
    }
}