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

    @Query("SELECT m.emisor.id, COUNT(m) FROM Mensaje m WHERE m.receptor.id = :idUsuario AND m.leido = false GROUP BY m.emisor.id")
    List<Object[]> contarMensajesNoLeidosPorContacto(@Param("idUsuario") Long idUsuario);

    List<Mensaje> findByReceptorIdAndEmisorIdAndLeidoFalse(Long receptorId, Long emisorId);

    @Query("""
    SELECT CASE WHEN m.emisor.id = :idUsuario THEN m.receptor.id ELSE m.emisor.id END,
           m.texto,
           m.fechaEnvio
    FROM Mensaje m
    WHERE (m.emisor.id = :idUsuario OR m.receptor.id = :idUsuario)
      AND m.id = (
          SELECT MAX(m2.id) FROM Mensaje m2
          WHERE (m2.emisor.id = m.emisor.id AND m2.receptor.id = m.receptor.id)
             OR (m2.emisor.id = m.receptor.id AND m2.receptor.id = m.emisor.id)
      )
    """)
    List<Object[]> findUltimoMensajePorContacto(@Param("idUsuario") Long idUsuario);
}
