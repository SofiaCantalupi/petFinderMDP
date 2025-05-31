package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_finder.models.Comentario;

@Repository
public interface ComentarioRepositories extends JpaRepository<Comentario, Long> {



}
