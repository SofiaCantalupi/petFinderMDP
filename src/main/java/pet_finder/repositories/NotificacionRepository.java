package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.models.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {

}
