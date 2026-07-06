package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.models.Miembro;
import pet_finder.models.SolicitudAdopcion;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {
    boolean existsByIdPublicacionAndMiembroSolicitante(Long idPublicacion, Miembro miembroSolicitante);
}
