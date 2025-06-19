package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.*;
import pet_finder.models.*;
import pet_finder.services.ComentarioServices;
import pet_finder.services.MascotaService;
import pet_finder.services.MiembroService;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Component
public class PublicacionMapper implements Mapper<PublicacionRequestDTO, PublicacionDetailDTO, Publicacion> {

    private final MascotaService mascotaService;
    private final MiembroService miembroService;
    private final ComentarioServices comentarioService;

    private final UbicacionMapper ubicacionMapper;
    private final MascotaMapper mascotaMapper;
    private final ComentarioMapper comentarioMapper;

    public PublicacionMapper (MascotaService mascotaService,
                              MiembroService miembroService,
                              ComentarioServices comentarioService,
                              UbicacionMapper ubicacionMapper, MascotaMapper mascotaMapper, ComentarioMapper comentarioMapper) {
        this.mascotaService = mascotaService;
        this.miembroService = miembroService;
        this.comentarioService = comentarioService;
        this.ubicacionMapper = ubicacionMapper;
        this.mascotaMapper = mascotaMapper;
        this.comentarioMapper = comentarioMapper;
    }

    @Override
    public Publicacion aEntidad(PublicacionRequestDTO request) {

        // Se crea una nueva Publicacion, con los datos recibidos del Request
        Publicacion publicacion = new Publicacion();
        publicacion.setDescripcion(request.getDescripcion());

        Mascota mascota = mascotaService.obtenerPorId(request.getMascotaId());
        publicacion.setMascota(mascota);

        publicacion.setUbicacion(ubicacionMapper.aEntidad(request.getUbicacion()));

        return publicacion;
    }

    @Override
    public PublicacionDetailDTO aDetail(Publicacion publicacion) {

        List<ComentarioDetailDTO> comentarioDetailDTOS = publicacion.getComentarios()// obtengo los comentarios asociados a la publicacion
                .stream()
                .filter(Comentario::getActivo) // primero filtro los comentarios activos
                .map(comentarioMapper::aDetail)  // luego los convierto a DTO
                .toList();

        // Se utiliza constructor del record DetailDTO
        return new PublicacionDetailDTO(publicacion, comentarioDetailDTOS);
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
        entidad.setDescripcion(request.getDescripcion());

        // todo: Añadir imagen

        Mascota mascota = mascotaService.obtenerPorId(request.getMascotaId());
        entidad.setMascota(mascota);

        // Trae la ubicacion del request
        Ubicacion ubicacion = ubicacionMapper.aEntidad(request.getUbicacion());
        entidad.setUbicacion(ubicacion);

        return entidad; // retorna la entidad actualizada
    }
}