package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.*;
import pet_finder.models.*;
import pet_finder.services.ComentarioServices;
import pet_finder.services.MascotaService;
import pet_finder.services.MiembroService;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.UbicacionValidation;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Component
public class PublicacionMapper implements Mapper<PublicacionRequestDTO, PublicacionDetailDTO, Publicacion> {

    private final MascotaService mascotaService;
    private final UbicacionMapper ubicacionMapper;
    private final ComentarioMapper comentarioMapper;
    private final MiembroValidation miembroValidation;
    private final UbicacionValidation ubicacionValidation;

    public PublicacionMapper (MascotaService mascotaService,
                              UbicacionMapper ubicacionMapper, ComentarioMapper comentarioMapper, MiembroValidation miembroValidation, UbicacionValidation ubicacionValidation) {
        this.mascotaService = mascotaService;
        this.ubicacionMapper = ubicacionMapper;
        this.comentarioMapper = comentarioMapper;
        this.miembroValidation = miembroValidation;
        this.ubicacionValidation = ubicacionValidation;
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

    public Publicacion modificar(Publicacion entidad, PublicacionRequestUpdateDTO request, Long idMiembroLogeado) {

        // Validación de que el miembro logueado sea el dueño de la publicación
        miembroValidation.estaLogeado(entidad.getIdMiembro(), idMiembroLogeado);

        if (request.getDescripcion() == null && request.getUbicacion() == null) {
            throw new IllegalArgumentException("Debe proporcionar al menos una descripción o una ubicación para modificar la publicación.");
        }


        // Si se envió una nueva descripción, y es distinta de la actual se actualiza
        if (request.getDescripcion() != null && !request.getDescripcion().trim().isEmpty()) {
            if (!request.getDescripcion().equals(entidad.getDescripcion())) {
                entidad.setDescripcion(request.getDescripcion());
            }
        }

        // Si se envió una nueva ubicación  se compara con la actual y se actualiza si son diferentes
        if (request.getUbicacion() != null) {
            if (!ubicacionValidation.contenidoIgualA(entidad.getUbicacion(), request.getUbicacion())) {
                entidad.setUbicacion(ubicacionMapper.aEntidad(request.getUbicacion()));
            }
        }

        return entidad;
    }

//    // este metodo toma el request y la entidad que se quiere modificar, actualiza los datos en la entidad existente y retorna la entidad modificada.
//    public Publicacion modificar (Publicacion entidad, PublicacionRequestUpdateDTO request,Long idMiembroLogeado) {
//
//        miembroValidation.estaLogeado(entidad.getIdMiembro(),idMiembroLogeado);
//
//        if(!entidad.getDescripcion().equals(request.getDescription())){
//            entidad.setDescripcion(request.getDescription());
//        }
//        if(!ubicacionValidation.contenidoIgualA(entidad.getUbicacion(),request.getUbicacion())){
//            entidad.setUbicacion(ubicacionMapper.aEntidad(request.getUbicacion()));
//        }
//
//        return entidad; // retorna la entidad actualizada
//    }
}