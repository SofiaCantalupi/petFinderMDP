package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.enums.TipoNotificacion;
import pet_finder.models.Notificacion;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {

    List<Notificacion> findByReceptorIdAndActivaTrue(Long receptorId);
    long countByReceptorIdAndActivaTrueAndLeidaFalse(Long receptorId);
    List<Notificacion> findByEntidadReferenciaId(Long entidadReferenciaId);
    List<Notificacion> findByTipoAndEntidadReferenciaIdAndActivaTrue(TipoNotificacion tipo, Long referenciaId);

    //Evita volver a mandar una notificacion si ya se mandó anteriormente. Especificamente para cuando vuelve a estar en adopción y ya se habia notificado.
    boolean existsByTipoAndEntidadReferenciaId(TipoNotificacion tipo, Long entidadReferenciaId);


}
