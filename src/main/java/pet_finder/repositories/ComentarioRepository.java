package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_finder.models.Comentario;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findByPublicacionIdAndActivoTrue(Long idPublicacion);

    //Trae los comentarios que escribio un miembro, sin importar de quien sea la publicacion.
    List<Comentario> findByMiembroIdAndActivoTrue(Long idMiembro);

}
