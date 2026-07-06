package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.publicacion.PublicacionDetailDTO;
import pet_finder.dtos.publicacion.PublicacionRequestDTO;
import pet_finder.dtos.ubicacion.UbicacionDetailDTO;
import pet_finder.dtos.miembro.MiembroDetailDTO;
import pet_finder.models.*;
import pet_finder.services.MascotaService;
import pet_finder.services.MiembroService;

import java.util.List;

@Component
public class PublicacionMapper implements Mapper<PublicacionRequestDTO, PublicacionDetailDTO, Publicacion> {

    private final MascotaService mascotaService;
    private final MiembroService miembroService;

    private final UbicacionMapper ubicacionMapper;
    private final ComentarioMapper comentarioMapper;
    private final MascotaMapper mascotaMapper;
    private final MiembroMapper miembroMapper;


    public PublicacionMapper (MascotaService mascotaService,
                              UbicacionMapper ubicacionMapper,
                              ComentarioMapper comentarioMapper,
                              MiembroService miembroService,
                              MascotaMapper mascotaMapper,
                              MiembroMapper miembroMapper) {
        this.mascotaService = mascotaService;
        this.ubicacionMapper = ubicacionMapper;
        this.comentarioMapper = comentarioMapper;
        this.miembroService = miembroService;
        this.mascotaMapper = mascotaMapper;
        this.miembroMapper = miembroMapper;
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

        //Obtengo el miembro 
        Miembro miembro = miembroService.obtenerPorId(publicacion.getMiembro().getId());

        // Conversion de entidades a DTOs
        MiembroDetailDTO miembroDTO = miembroMapper.aDetail(miembro);
        MascotaDetailDTO mascotaDTO = mascotaMapper.aDetail(publicacion.getMascota());
        UbicacionDetailDTO ubicacionDTO = publicacion.getUbicacion() != null
            ? ubicacionMapper.aDetail(publicacion.getUbicacion())
            : null;

        return new PublicacionDetailDTO(publicacion, miembroDTO, mascotaDTO, ubicacionDTO, comentarioDetailDTOS);
    }

    @Override
    public List<PublicacionDetailDTO> deEntidadesAdetails(List<Publicacion> entidades) {
        // Cada Publicacion de la lista se mappea a Detail
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }
}