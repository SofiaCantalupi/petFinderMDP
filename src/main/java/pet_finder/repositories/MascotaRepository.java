package pet_finder.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pet_finder.models.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}
