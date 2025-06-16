package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pet_finder.models.NormaComunidad;

public interface NormaComunidadRepository extends JpaRepository<NormaComunidad, Long> {
}
