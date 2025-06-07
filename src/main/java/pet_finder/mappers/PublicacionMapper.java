package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.*;
import pet_finder.models.Mascota;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.models.Ubicacion;
import pet_finder.services.MascotaService;
import pet_finder.services.MiembroService;
import pet_finder.validations.UbicacionValidation;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Component
public class PublicacionMapper implements Mapper<PublicacionRequestDTO, PublicacionDetailDTO, Publicacion> {

    private final MascotaService mascotaService;
    private final MiembroService miembroService;

    private final UbicacionValidation ubicacionValidation;

    public PublicacionMapper (MascotaService mascotaService, MiembroService miembroService, UbicacionValidation ubicacionValidation) {
        this.mascotaService = mascotaService;
        this.miembroService = miembroService;
        this.ubicacionValidation = ubicacionValidation;
    }

    @Override
    public Publicacion aEntidad(PublicacionRequestDTO request) {
        // Se crea una nueva Publicacion, con los datos recibidos del Request
        Publicacion publicacion = new Publicacion();
        publicacion.setDescripcion(request.descripcion());

        // todo: Añadir link de imagen

        // Valida la existencia de la Mascota
        Mascota mascota = mascotaService.obtenerPorId(request.mascotaId());
        publicacion.setMascota(mascota);

        // Valida la existencia del Miembro
        // todo: Necesito una Entidad, no necesito un DTO
        Miembro miembro = miembroService.obtenerPorId(request.miembroId());
        publicacion.setMiembro(miembro);

        // Valida si la Ubicacion se puede geocodificar (realmente existe)
        ubicacionValidation.validarGeocodificacion(request.ubicacion());
        // Crea una nueva Ubicacion y la setea
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setPais(request.ubicacion().pais());
        ubicacion.setRegion(request.ubicacion().region());
        ubicacion.setCiudad(request.ubicacion().ciudad());
        ubicacion.setDireccion(request.ubicacion().direccion());
        ubicacion.setAltura(request.ubicacion().altura());
        publicacion.setUbicacion(ubicacion);

        return publicacion;
    }

    @Override
    public PublicacionDetailDTO aDetail(Publicacion entidad) {
        // Se utiliza constructor del record DetailDTO para retornar un nuevo DetailDTO con los valores de la Publicacion recibida
        return new PublicacionDetailDTO(
                entidad.getId(), entidad.getDescripcion(),
                entidad.getFecha(), entidad.getActivo(),
                new MascotaDetailDTO(entidad.getMascota()),
                entidad.getMiembro().getId(), entidad.getMiembro().getEmail(),
                entidad.getUbicacion().getDireccion(), entidad.getUbicacion().getAltura(),
                entidad.getUbicacion().getCiudad(), entidad.getUbicacion().getRegion(), entidad.getUbicacion().getPais());
    }

    @Override
    public List<PublicacionDetailDTO> deEntidadesAdetails(List<Publicacion> entidades) {
        // Cada Publicacion de la lista se mappea a Detail
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }

    // este metodo toma el request y la entidad que se quiere modificar, actualiza los datos en la entidad existente y retorna la entidad modificada.
    @Override
    public Publicacion modificar (Publicacion entidad, PublicacionRequestDTO request) {
        // Se toma la entidad que se quiere modificar y se actualiza con los datos del RequestDTO
        entidad.setDescripcion(request.descripcion());

        // todo: Añadir link de imagen
        // Se setea la Ubicacion anterior
        Mascota mascota = mascotaService.obtenerPorId(request.mascotaId());
        entidad.setMascota(mascota);

        // todo: Necesito una Entidad Miembro, no un DTO
        Miembro miembro = miembroService.obtenerPorId(request.miembroId());
        entidad.setMiembro(miembro);

        // Se setea la Ubicacion anterior
        entidad.setUbicacion(entidad.getUbicacion());

        // Valida si la Ubicacion recibida por DTO se puede geocodificar (realmente existe)
        ubicacionValidation.validarGeocodificacion(request.ubicacion());

        // Se modifica la Ubicacion con los nuevos datos recibidos del DTO
        entidad.getUbicacion().setDireccion(request.ubicacion().direccion());
        entidad.getUbicacion().setAltura(request.ubicacion().altura());
        entidad.getUbicacion().setCiudad(request.ubicacion().ciudad());
        entidad.getUbicacion().setRegion(request.ubicacion().region());
        entidad.getUbicacion().setPais(request.ubicacion().pais());

        return entidad; // retorna la entidad actualizada
    }
}