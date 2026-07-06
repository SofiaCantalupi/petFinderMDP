package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.models.SolicitudAdopcion;
import pet_finder.repositories.SolicitudAdopcionRepository;

@Service
public class SolicitudAdopcionService {
    private final SolicitudAdopcionRepository solicitudRepository;

    public SolicitudAdopcionService(SolicitudAdopcionRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    public SolicitudAdopcion guardar(SolicitudAdopcion solicitud){
        return solicitudRepository.save(solicitud);
    }
}
