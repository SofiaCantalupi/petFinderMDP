package pet_finder.validations;

import org.springframework.stereotype.Component;
import pet_finder.enums.EstadoMascota;
import pet_finder.models.Miembro;
import pet_finder.repositories.SolicitudAdopcionRepository;

@Component
public class SolicitudAdopcionValidation {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    public SolicitudAdopcionValidation(SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
    }

    public void validarEstadoMascotaParaAdopcion(EstadoMascota estadoMascota){
        if(!estadoMascota.equals(EstadoMascota.EN_ADOPCION)){
            throw new IllegalArgumentException("La mascota de la publicación no está disponible para adopción.");
        }
    }

    public void validarQueNoSeaDuenioPublicacion(Long idMiembroSolicitante, Long idMiembroPublicacion){
        if(idMiembroPublicacion.equals(idMiembroSolicitante)){
            throw new IllegalArgumentException("No podés solicitar la adopción de tu propia mascota publicada.");
        }
    }

    public void validarSinSolicitudBloqueante(Long idPublicacion, Miembro miembroSolicitante){
        if(solicitudAdopcionRepository.existsByIdPublicacionAndMiembroSolicitante(idPublicacion, miembroSolicitante)){
            throw new IllegalArgumentException("Ya tenés una solicitud de adopción asociada a esta publicación.");
        }
    }
}
