package pet_finder.repositories;

import pet_finder.models.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro,Long> {
    Optional<Miembro> findByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);       //Verifica que exista el mail registrado pero con otro ID (para los updates)
}
