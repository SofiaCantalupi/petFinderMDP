package pet_finder.repositories;

import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    List<Publicacion> findAllByActivoTrue();

    // Busca todas las publicaciones donde la mascota coincida con el tipo de mascota recibido por parametro
    List<Publicacion> findAllByMascotaTipoMascota(TipoMascota tipoMascota);

    // Busca todas las publicaciones donde la mascota coincida con el estado recibido por parametro
    List<Publicacion> findAllByMascotaEstadoMascota(EstadoMascota estadoMascota);
}
