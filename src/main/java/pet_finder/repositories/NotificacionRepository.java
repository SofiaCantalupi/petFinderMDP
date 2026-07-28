package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.models.Notificacion;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {

    List<Notificacion> findByReceptorIdAndActivaTrue(Long receptorId);
    long countByReceptorIdAndActivaTrueAndLeidaFalse(Long receptorId);
}
