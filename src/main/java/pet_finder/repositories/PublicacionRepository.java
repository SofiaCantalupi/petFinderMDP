package pet_finder.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.models.Miembro;
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

    // Busca todas las publicaciones donde la mascota coincida con el tipo y estado recibido por parametro
    List<Publicacion> findAllByMascotaTipoMascotaAndMascotaEstadoMascota(TipoMascota tipo, EstadoMascota estado);

    // Busca todas las publicaciones donde la publicacion coincida con el ID miembro
    List<Publicacion> findAllByMiembro_Id(Long miembroId);

    boolean existsByMascotaId(Long mascotaId);

    List<Publicacion> findByMiembroAndActivoTrue(Miembro miembro);

}
