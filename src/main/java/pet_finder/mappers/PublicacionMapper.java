package pet_finder.mappers;

import org.springframework.stereotype.Component;
import pet_finder.dtos.*;
import pet_finder.models.*;
import pet_finder.repositories.MiembroRepository;
import pet_finder.services.MascotaService;
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

        //Obtengo el miembro para obtener su nombre completo:
        Miembro miembro = miembroValidation.validarExistenciaPorId(publicacion.getIdMiembro());

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

    //Se le pasa la publicación original, los campos con la información que se quiere actualizar
    //dentro de un DTO y el miembro del miembro autenticado.
    public Publicacion modificar(Publicacion entidad, PublicacionRequestUpdateDTO request, Long idMiembroLogeado) {

        // Validación de que el miembro logueado sea el dueño de la publicación
        miembroValidation.estaLogeado(entidad.getIdMiembro(), idMiembroLogeado);

        //Verifican si los campos vienen vacios o solo con espacios blancos (isBlank)
        boolean descripcionVacia = request.getDescripcion() == null  || request.getDescripcion().isBlank();
        boolean ubicacionVacia = request.getUbicacion() == null;

        //Si ambos estan vacios no tiene sentido el update, así que se lanza una excepción.
        if (descripcionVacia && ubicacionVacia) {
            throw new IllegalArgumentException("Debe proporcionar al menos una descripción o una ubicación para modificar la publicación.");
        }

        // Si la descripción nueva contiene contenido que no sea la descripción actual
        //se actualiza la publicación con la nueva descripción.
        if (!descripcionVacia && !request.getDescripcion().equals(entidad.getDescripcion())) {
                entidad.setDescripcion(request.getDescripcion());
        }

        // Si la ubicación nueva contiene contenido que no sea la ubicación actual
        //se actualiza la publicación con la nueva ubicación.
        if (!ubicacionVacia && !ubicacionValidation.contenidoIgualA(entidad.getUbicacion(), request.getUbicacion())) {
                entidad.setUbicacion(ubicacionMapper.aEntidad(request.getUbicacion()));
        }

        //Se retorna la publicación con los cambios hechos.
        return entidad;
    }

}