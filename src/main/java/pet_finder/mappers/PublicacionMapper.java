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

        // todo: Añadir imagen

        // Trae la Mascota vinculada
        Mascota mascota = mascotaService.obtenerPorId(request.getMascotaId());
        publicacion.setMascota(mascota);

        // El set miembroID ocurre en el controller

        // Genera una nueva Ubicacion y la valida
        Ubicacion ubicacion = ubicacionMapper.aEntidad(request.getUbicacion());

        publicacion.setUbicacion(ubicacion);

        return publicacion;
    }

    @Override
    public PublicacionDetailDTO aDetail(Publicacion publicacion) {
        MascotaDetailDTO mascotaDetailDTO = mascotaMapper.aDetail(publicacion.getMascota());
        List<ComentarioDetailDTO> comentarioDetailDTOS = comentarioMapper.deEntidadesAdetails(publicacion.getComentarios());
        UbicacionDetailDTO ubicacionDetailDTO = ubicacionMapper.aDetail(publicacion.getUbicacion());

        // Se utiliza constructor del record DetailDTO
        return new PublicacionDetailDTO(publicacion, mascotaDetailDTO, publicacion.getIdMiembro(), ubicacionDetailDTO, comentarioDetailDTOS);
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