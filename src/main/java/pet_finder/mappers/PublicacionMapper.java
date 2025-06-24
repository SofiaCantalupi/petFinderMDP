package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.dtos.publicacion.PublicacionDetailDTO;
import pet_finder.dtos.publicacion.PublicacionRequestDTO;
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


    public PublicacionMapper (MascotaService mascotaService,
                              UbicacionMapper ubicacionMapper, ComentarioMapper comentarioMapper, MiembroService miembroService) {
        this.mascotaService = mascotaService;
        this.ubicacionMapper = ubicacionMapper;
        this.comentarioMapper = comentarioMapper;
        this.miembroService = miembroService;
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

        //Obtengo el miembro para obtener su nombre completo:
        Miembro miembro = miembroService.obtenerPorId(publicacion.getMiembro().getId());

        //Obtengo el nombre completo del miembro para mostrarlo:
        String nombreCompleto = miembro.getNombre() + " " + miembro.getApellido();

        //Obtengo la mascota entera para mostrarla en el detail.
        Mascota mascota = publicacion.getMascota();

        // Se utiliza constructor del record DetailDTO
        return new PublicacionDetailDTO(nombreCompleto,publicacion,mascota,comentarioDetailDTOS);
    }

    @Override
    public List<PublicacionDetailDTO> deEntidadesAdetails(List<Publicacion> entidades) {
        // Cada Publicacion de la lista se mappea a Detail
        return entidades.stream()
                .map(this::aDetail)
                .toList();
    }
}