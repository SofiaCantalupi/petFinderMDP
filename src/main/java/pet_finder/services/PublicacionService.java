package pet_finder.services;

import org.springframework.context.annotation.Lazy;
import pet_finder.dtos.publicacion.PublicacionDetailDTO;
import pet_finder.dtos.publicacion.PublicacionRequestDTO;
import pet_finder.dtos.publicacion.PublicacionRequestUpdateDTO;
import pet_finder.enums.EstadoMascota;
import pet_finder.enums.MotivoRechazo;
import pet_finder.enums.TipoMascota;
import pet_finder.mappers.PublicacionMapper;
import pet_finder.mappers.UbicacionMapper;
import pet_finder.models.Comentario;
import pet_finder.models.Mascota;
import pet_finder.models.Publicacion;
import pet_finder.repositories.PublicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet_finder.validations.MascotaValidation;
import pet_finder.validations.MiembroValidation;
import pet_finder.validations.PublicacionValidation;
import pet_finder.validations.UbicacionValidation;
import java.util.Optional;

import java.util.List;


@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    private final UbicacionService ubicacionService;
    private final MascotaService mascotaService;
    private final ComentarioService comentarioService;
    private final SolicitudAdopcionService solicitudService;

    private final PublicacionValidation publicacionValidation;
    private final MiembroValidation miembroValidation;
    private final MascotaValidation mascotaValidation;
    private final UbicacionValidation ubicacionValidation;

    private final UbicacionMapper ubicacionMapper;
    private final PublicacionMapper publicacionMapper;

    private final NotificacionService notificacionService;

    public PublicacionService(PublicacionRepository publicacionRepository, UbicacionService ubicacionService, MascotaService mascotaService, ComentarioService comentarioService, @Lazy SolicitudAdopcionService solicitudService, PublicacionValidation publicacionValidation, MiembroValidation miembroValidation, MascotaValidation mascotaValidation, UbicacionValidation ubicacionValidation, UbicacionMapper ubicacionMapper, PublicacionMapper publicacionMapper, NotificacionService notificacionService) {
        this.publicacionRepository = publicacionRepository;
        this.ubicacionService = ubicacionService;
        this.mascotaService = mascotaService;
        this.comentarioService = comentarioService;
        this.solicitudService = solicitudService;
        this.publicacionValidation = publicacionValidation;
        this.miembroValidation = miembroValidation;
        this.mascotaValidation = mascotaValidation;
        this.ubicacionValidation = ubicacionValidation;
        this.ubicacionMapper = ubicacionMapper;
        this.publicacionMapper = publicacionMapper;
        this.notificacionService = notificacionService;
    }


    //Nueva publicacion.
@Transactional
public PublicacionDetailDTO guardar(PublicacionRequestDTO request, Long idMiembro) {

    Publicacion publicacion = publicacionMapper.aEntidad(request);

    // Se valida que la mascota este activa
    mascotaValidation.esActivo(publicacion.getMascota().getEsActivo());

    // Se valida que la mascota pertenezca al miembro que esta publicando
    miembroValidation.estaLogeado(publicacion.getMascota().getMiembroId(), idMiembro);

    // Se valida que la mascota no pertenezca a otra publicacion
    publicacionValidation.mascotaYaAsignada(publicacion.getMascota().getId());

    // Se valida que la ubicacion pueda ser geocodificada
    ubicacionValidation.validarGeocodificacion(publicacion.getUbicacion());

    //Se valida que el miembro exista y se lo asocia a la publicación.
    publicacion.setMiembro(miembroValidation.validarExistenciaPorId(idMiembro));

    Publicacion guardada = publicacionRepository.save(publicacion);
    return publicacionMapper.aDetail(guardada);
}

    public Publicacion obtenerPorId(Long id) {

        // Valida si existe la Publicacion con ese id, si existe la retorna
        Publicacion existente = publicacionValidation.existePorId(id);

        // Valida si la Publicacion esta activa
        publicacionValidation.esActivo(existente.getActivo());
        return existente;
    }

    @Transactional(readOnly = true)
    public PublicacionDetailDTO obtenerDetallePorId(Long id) {
        return publicacionMapper.aDetail(obtenerPorId(id));
    }

    // LISTAR TODAS LAS PUBLICACIONES
    public List<Publicacion> listarTodas() {
        return publicacionRepository.findAll();
    }

    // LISTAR LAS PUBLICACIONES ACTIVAS
    @Transactional(readOnly = true)
    public List<PublicacionDetailDTO> listarActivas() {
        return publicacionMapper.deEntidadesAdetails(publicacionRepository.findAllByActivoTrue());
    }

    // Listar publicaciones de un miembro
    @Transactional(readOnly = true)
    public List<PublicacionDetailDTO> listarPropias(Long miembroId){
        return publicacionMapper.deEntidadesAdetails(publicacionRepository.findByMiembroIdAndActivoTrue(miembroId));
    }

    // FILTRAR POR TipoMascota
    @Transactional(readOnly = true)
    public List<PublicacionDetailDTO> filtrarPorTipoMascota(String tipoString){

        // El controller recibe un String, por lo tanto debe convertirse a un dato tipo Enum (TipoMascota)
        // Se valida que el string sea valido ("gato" o "perro") y se convierte a su respectivo Enum (TipoMascota)
        TipoMascota tipoEnum = mascotaValidation.validarYConvertirTipoMascota(tipoString);

        // Se buscan las publicaciones cuyas mascotas son del tipo ingresado por parametro, y se filtran las publicaciones activas
        List<Publicacion> publicaciones = publicacionRepository.findAllByMascotaTipoMascota(tipoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();

        return publicacionMapper.deEntidadesAdetails(publicaciones);
    }

    // FILTRAR POR EstadoMascota
    @Transactional(readOnly = true)
    public List<PublicacionDetailDTO> filtrarPorEstadoMascota(String estadoString){

        // Se valida que el string sea valido ("perdido" o "encontrado") y se convierte a su respectivo Enum (EstadoMascota)
        EstadoMascota estadoEnum = mascotaValidation.validarYConvertirEstadoMascota(estadoString);

        // El metodo encuentra todas las publicaciones con ese estado y filtra las publicaciones activas.
        List<Publicacion> publicaciones = publicacionRepository.findAllByMascotaEstadoMascota(estadoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();

        return publicacionMapper.deEntidadesAdetails(publicaciones);
    }

    // FILTRAR POR TipoMascota y EstadoMascota
    @Transactional(readOnly = true)
    public List<PublicacionDetailDTO> filtrarPorTipoYEstado(String tipo, String estado) {
        // Validar y convertir ambos strings a Enum
        TipoMascota tipoEnum = mascotaValidation.validarYConvertirTipoMascota(tipo);
        EstadoMascota estadoEnum = mascotaValidation.validarYConvertirEstadoMascota(estado);

        // Buscar publicaciones con ese tipo y estado
        List<Publicacion> publicaciones = publicacionRepository
                .findAllByMascotaTipoMascotaAndMascotaEstadoMascota(tipoEnum, estadoEnum)
                .stream()
                .filter(Publicacion::getActivo)
                .toList();

        return publicacionMapper.deEntidadesAdetails(publicaciones);
    }

    // Modificar una publicacion
    @Transactional
    public PublicacionDetailDTO modificar(Long publicacionId, Long miembroLogeadoId, PublicacionRequestUpdateDTO request) {

        // Se obtiene la publicacion que se quiere modificar, se valida que exista y este activa
        Publicacion existente = obtenerPorId(publicacionId);

        // Validación de que el miembro logueado sea el dueño de la publicación
        miembroValidation.estaLogeado(existente.getMiembro().getId(), miembroLogeadoId);

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
        return publicacionMapper.aDetail(existente);
    }


    
  // Modificar el estado de la mascota de una publicacion (endpoint genérico).
// No permite marcar como ADOPTADA: eso solo puede pasar al resolver una solicitud.
@Transactional
public PublicacionDetailDTO modificarEstado(Long publicacionId,
                                            Long miembroLogeadoId,
                                            String estado) {

    EstadoMascota nuevoEstado = mascotaValidation.validarYConvertirEstadoMascota(estado);

    publicacionValidation.validarCambioEstadoManual(nuevoEstado);

    return cambiarEstadoMascota(publicacionId, miembroLogeadoId, nuevoEstado);
}

// Uso exclusivo de SolicitudAdopcionService al aprobar una solicitud.
@Transactional
public PublicacionDetailDTO marcarComoAdoptada(Long publicacionId, Long miembroLogeadoId) {
    return cambiarEstadoMascota(publicacionId, miembroLogeadoId, EstadoMascota.ADOPTADA);
}


private PublicacionDetailDTO cambiarEstadoMascota(Long publicacionId,
                                                  Long miembroLogeadoId,
                                                  EstadoMascota nuevoEstado) {

    Publicacion existente = obtenerPorId(publicacionId);

    miembroValidation.estaLogeado(existente.getMiembro().getId(), miembroLogeadoId);

    Mascota mascota = existente.getMascota();
    EstadoMascota estadoAnterior = mascota.getEstadoMascota();

    mascotaValidation.validarCambioEstado(mascota, nuevoEstado);

    if (estadoAnterior == EstadoMascota.EN_ADOPCION &&
            (nuevoEstado == EstadoMascota.ENCONTRADA ||
                    nuevoEstado == EstadoMascota.PERDIDA)) {

        solicitudService.revertirPendientes(
                publicacionId,
                MotivoRechazo.AUTO_CAMBIO_ESTADO_MASCOTA);
    }

    if (nuevoEstado == EstadoMascota.EN_ADOPCION &&
            (estadoAnterior == EstadoMascota.ADOPTADA ||
                    estadoAnterior == EstadoMascota.ENCONTRADA ||
                    estadoAnterior == EstadoMascota.PERDIDA)) {

        notificacionService.notificarSolicitantesPorAdopcionDisponible(publicacionId);
    }

    mascota.setEstadoMascota(nuevoEstado);
    publicacionRepository.save(existente);

    return publicacionMapper.aDetail(existente);
}



    // Eliminar una publicacion
    @Transactional
    public void eliminar(Publicacion publicacion) {

        // Baja logica de la mascota asociada
mascotaService.eliminar(publicacion.getMascota().getId(), publicacion.getMiembro().getId());

        // Baja logica de la ubicacion
        ubicacionService.eliminar(publicacion.getUbicacion().getId());

        // Baja logica de cada comentario
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(publicacion.getId());
        comentarios.forEach(comentario -> comentarioService.eliminarComentarioPorId(comentario.getId()));

        // Se cambia el estado de las solicitudes de asociadas de pendiente a rechazadas
        solicitudService.revertirPendientes(publicacion.getId(), MotivoRechazo.AUTO_POR_PUBLICACION_ELIMINADA);

        // Baja logica de la publicacion en si
        publicacion.setActivo(false);
        publicacionRepository.save(publicacion);
    }

    //Eliminar publicación como miembro.
    public void eliminarPublicacionPropia(Publicacion publicacion,Long idMiembroLogeado){

        //Si el ID del miembro autenticado y el del miembro no coinciden
        //se lanza una excepción ya que se estaria tratando de borrar una publicación
        //que no es del usuario autenticado.
        miembroValidation.estaLogeado(publicacion.getMiembro().getId(),idMiembroLogeado);

        this.eliminar(publicacion);
    }

    // Método para buscar una publicación por el ID de la mascota asociada
public Optional<Publicacion> buscarPorMascotaId(Long mascotaId) {
    return publicacionRepository.findByMascotaId(mascotaId);
}

}
