package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pet_finder.models.Mensaje;

import java.util.List;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m WHERE ((m.emisor.id = :idUsuario AND m.receptor.id = :idOtro) OR (m.emisor.id = :idOtro AND m.receptor.id = :idUsuario)) AND m.id > :desdeId ORDER BY m.id ASC")
    List<Mensaje> findConversacionDesde(@Param("idUsuario") Long idUsuario, @Param("idOtro") Long idOtro, @Param("desdeId") Long desdeId);

    @Query("SELECT DISTINCT m.receptor.id FROM Mensaje m WHERE m.emisor.id = :idUsuario")
    List<Long> findIdsReceptores(@Param("idUsuario") Long idUsuario);

    @Query("SELECT DISTINCT m.emisor.id FROM Mensaje m WHERE m.receptor.id = :idUsuario")
    List<Long> findIdsEmisores(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.receptor.id = :idUsuario AND m.emisor.id = :idOtro AND m.leido = false")
    Long countMensajesNoLeidos(@Param("idUsuario") Long idUsuario, @Param("idOtro") Long idOtro);
}
