package pet_finder.services;

import pet_finder.dtos.publicacion.PublicacionRequestUpdateDTO;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.TipoMascota;
import pet_finder.mappers.UbicacionMapper;
import pet_finder.models.Comentario;
import pet_finder.models.Publicacion;
import pet_finder.repositories.PublicacionRepository;
import org.springframework.stereotype.Service;
import pet_finder.validations.MascotaValidation;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.PublicacionValidation;
import pet_finder.validations.UbicacionValidation;

import java.util.List;


@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    private final UbicacionService ubicacionService;
    private final MascotaService mascotaService;
    private final ComentarioService comentarioService;

    private final PublicacionValidation publicacionValidation;
    private final MiembroValidation miembroValidation;
    private final MascotaValidation mascotaValidation;
    private final UbicacionValidation ubicacionValidation;

    private final UbicacionMapper ubicacionMapper;

    public PublicacionService(PublicacionRepository publicacionRepository, UbicacionService ubicacionService, MascotaService mascotaService, ComentarioService comentarioService, PublicacionValidation publicacionValidation, MiembroValidation miembroValidation, MascotaValidation mascotaValidation, UbicacionValidation ubicacionValidation, UbicacionMapper ubicacionMapper) {
        this.publicacionRepository = publicacionRepository;
        this.ubicacionService = ubicacionService;
        this.mascotaService = mascotaService;
        this.comentarioService = comentarioService;
        this.publicacionValidation = publicacionValidation;
        this.miembroValidation = miembroValidation;
        this.mascotaValidation = mascotaValidation;
        this.ubicacionValidation = ubicacionValidation;
        this.ubicacionMapper = ubicacionMapper;
    }


    //Nueva publicacion.
    public Publicacion guardar(Publicacion publicacion) {

        // Se valida que la mascota este activa
        mascotaValidation.esActivo(publicacion.getMascota().getEsActivo());

        // Se valida que la mascota no pertenezca a otra publicacion
        publicacionValidation.mascotaYaAsignada(publicacion.getMascota().getId());

        // Se valida que la ubicacion pueda ser geocodificada
        ubicacionValidation.validarGeocodificacion(publicacion.getUbicacion());

        return publicacionRepository.save(publicacion);
    }

    public Publicacion obtenerPorId(Long id) {

        // Valida si existe la Publicacion con ese id, si existe la retorna
        Publicacion existente = publicacionValidation.existePorId(id);

        // Valida si la Publicacion esta activa
        publicacionValidation.esActivo(existente.getActivo());
        return existente;
    }

    // LISTAR TODAS LAS PUBLICACIONES
    public List<Publicacion> listarTodas() {
        return publicacionRepository.findAll();
    }

    // LISTAR LAS PUBLICACIONES ACTIVAS
    public List<Publicacion> listarActivas() {
        return publicacionRepository.findAllByActivoTrue();
    }

    // FILTRAR POR TipoMascota
    public List<Publicacion> filtrarPorTipoMascota(String tipoString){

        // El controller recibe un String, por lo tanto debe convertirse a un dato tipo Enum (TipoMascota)
        // Se valida que el string sea valido ("gato" o "perro") y se convierte a su respectivo Enum (TipoMascota)
        TipoMascota tipoEnum = mascotaValidation.validarYConvertirTipoMascota(tipoString);

        // Se buscan las publicaciones cuyas mascotas son del tipo ingresado por parametro, y se filtran las publicaciones activas
        return publicacionRepository.findAllByMascotaTipoMascota(tipoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();
    }

    // FILTRAR POR EstadoMascota
    public List<Publicacion> filtrarPorEstadoMascota(String estadoString){

        // Se valida que el string sea valido ("perdido" o "encontrado") y se convierte a su respectivo Enum (EstadoMascota)
        EstadoMascota estadoEnum = mascotaValidation.validarYConvertirEstadoMascota(estadoString);

        // El metodo encuentra todas las publicaciones con ese estado y filtra las publicaciones activas.
        return publicacionRepository.findAllByMascotaEstadoMascota(estadoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();
    }


    // Modifcar una publicacion
    public Publicacion modificar(Long publicacionId, Long miembroLogeadoId, PublicacionRequestUpdateDTO request) {

        // Se obtiene la publicacion que se quiere modificar, se valida que exista y este activa
        Publicacion existente = obtenerPorId(publicacionId);

        // Validación de que el miembro logueado sea el dueño de la publicación
        miembroValidation.estaLogeado(existente.getIdMiembro(), miembroLogeadoId);

        //Verifican si los campos vienen vacios o solo con espacios blancos (isBlank)
        boolean descripcionVacia = request.getDescripcion() == null  || request.getDescripcion().isBlank();
        boolean ubicacionVacia = request.getUbicacion() == null;

        //Si ambos estan vacios no tiene sentido el update, así que se lanza una excepción.
        if (descripcionVacia && ubicacionVacia) {
            throw new IllegalArgumentException("Debe proporcionar al menos una descripción o una ubicación para modificar la publicación.");
        }

        // Si la descripción nueva contiene contenido que no sea la descripción actual
        //se actualiza la publicación con la nueva descripción.
        if (!descripcionVacia && !request.getDescripcion().equals(existente.getDescripcion())) {
            existente.setDescripcion(request.getDescripcion());
        }

        // Si la ubicación nueva contiene contenido que no sea la ubicación actual
        //se actualiza la publicación con la nueva ubicación.
        if (!ubicacionVacia && !ubicacionValidation.contenidoIgualA(existente.getUbicacion(), request.getUbicacion())) {
            // Antes de settear la ubicacion nueva, se valida que pueda ser geocodificada
            ubicacionValidation.validarGeocodificacion(ubicacionMapper.aEntidad(request.getUbicacion()));
            existente.setUbicacion(ubicacionMapper.aEntidad(request.getUbicacion()));
        }

        //Se retorna la publicación con los cambios hechos.
        publicacionRepository.save(existente);
        return existente;
    }

    // Eliminar una publicacion
    public void eliminar(Long id) {
        // Valida si existe una publicacion por id y si esta activa
        Publicacion publicacion = obtenerPorId(id);

        // Baja logica de la mascota asociada
        mascotaService.eliminar(publicacion.getMascota().getId());

        // Baja logica de la ubicacion
        ubicacionService.eliminar(publicacion.getUbicacion().getId());

        // Baja logica de cada comentario
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(publicacion.getId());
        comentarios.forEach(comentario -> comentarioService.eliminarComentarioPorId(comentario.getId()));

        // Baja logica de la publicacion en si
        publicacion.setActivo(false);
        publicacionRepository.save(publicacion);
    }

    //Eliminar publicación como miembro.
    public void eliminarPublicacionPropia(Long idPublicacion,Long idMiembroLogeado){

        Publicacion publicacion = publicacionValidation.existePorId(idPublicacion);

        //Si el ID del miembro autenticado y el del miembro no coinciden
        //se lanza una excepción ya que se estaria tratando de borrar una publicación
        //que no es del usuario autenticado.
        miembroValidation.estaLogeado(publicacion.getIdMiembro(),idMiembroLogeado);

        this.eliminar(idPublicacion);
    }

}
