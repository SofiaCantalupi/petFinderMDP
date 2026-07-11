package pet_finder.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.mappers.SolicitudAdopcionMapper;
import pet_finder.models.Publicacion;
import pet_finder.models.SolicitudAdopcion;
import pet_finder.repositories.SolicitudAdopcionRepository;
import pet_finder.validations.SolicitudAdopcionValidation;
import pet_finder.models.Miembro;

@Service
public class SolicitudAdopcionService {
    private final SolicitudAdopcionRepository solicitudRepository;
    private final PublicacionService publicacionService;
    private final MiembroService miembroService;
    private final SolicitudAdopcionValidation solicitudValidation;
    private final SolicitudAdopcionMapper solicitudMapper;

    public SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository, PublicacionService publicacionService, MiembroService miembroService, SolicitudAdopcionValidation solicitudValidation, SolicitudAdopcionMapper solicitudMapper) {
        this.solicitudRepository = solicitudRepository;
        this.publicacionService = publicacionService;
        this.miembroService = miembroService;
        this.solicitudValidation = solicitudValidation;
        this.solicitudMapper = solicitudMapper;
    }

    @Transactional
    public SolicitudAdopcionDetailDTO guardar(SolicitudAdopcionRequestDTO request, Long idMiembroSolicitante){
        // Se obtiene la publicacion completa
        Publicacion publicacion = publicacionService.obtenerPorId(request.getIdPublicacion());
        // Se obtiene el ID del miembro que hizo la publicacion
        Long idMiembroDuenio = publicacion.getMiembro().getId();

        // Se obtiene el Miembro que realiza la solicitud
        Miembro miembroSolicitante = miembroService.obtenerPorId(idMiembroSolicitante);

        solicitudValidation.validarEstadoMascotaParaAdopcion(publicacion.getMascota().getEstadoMascota());
        solicitudValidation.validarQueNoSeaDuenioPublicacion(idMiembroSolicitante, idMiembroDuenio);
        solicitudValidation.validarSinSolicitudBloqueante(publicacion.getId(), idMiembroSolicitante);

        SolicitudAdopcion solicitud = solicitudMapper.aEntidad(request);
        solicitud.setPublicacion(publicacion);
        solicitud.setMiembroSolicitante(miembroSolicitante);

        SolicitudAdopcion guardada = solicitudRepository.save(solicitud);
        return solicitudMapper.aDetail(guardada);
    }
}
