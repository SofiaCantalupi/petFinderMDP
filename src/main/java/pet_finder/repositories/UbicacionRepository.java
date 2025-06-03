package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_finder.models.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion,Long> {
}
