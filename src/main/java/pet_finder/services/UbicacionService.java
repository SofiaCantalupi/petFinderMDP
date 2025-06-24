package pet_finder.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.models.Publicacion;
import pet_finder.models.Ubicacion;
import pet_finder.repositories.PublicacionRepository;
import pet_finder.repositories.UbicacionRepository;
import pet_finder.validations.UbicacionValidation;

import java.util.List;

/**
 * @author Daniel Herrera
 */
@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final PublicacionRepository publicacionRepository;

    private final UbicacionValidation ubicacionValidation;

    public UbicacionService(UbicacionRepository ubicacionRepository,
                            PublicacionRepository publicacionRepository,
                            UbicacionValidation ubicacionValidation) {
        this.ubicacionRepository = ubicacionRepository;
        this.publicacionRepository = publicacionRepository;
        this.ubicacionValidation = ubicacionValidation;
    }

    // NUEVA UBICACION
    public Ubicacion guardar (Ubicacion ubicacion) {
        return ubicacionRepository.save(ubicacion);
    }

    // LISTAR POR ID UBICACION
    public Ubicacion obtenerPorId (Long id) {
        // Valida si existe la Publicacion con ese id
        Ubicacion u = ubicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Ubicacion con el ID = "+id));
        // Valida si la Ubicacion es inactiva
        ubicacionValidation.esInactivo(u);
        return u;
    }

    // LISTAR POR ID DE PUBLICACION
    public Ubicacion obtenerPorIdPublicacion (Long id) {
        // Valida si existe la Publicacion con ese id
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));
        // Extrae la Ubicacion
        Ubicacion u = p.getUbicacion();

        // Valida si la Ubicacion es inactiva
        ubicacionValidation.esInactivo(u);

        return u;
    }

    // LISTAR TODAS LAS UBICACIONES
    public List<Ubicacion> listarTodas () {
        return ubicacionRepository.findAll();
    }

    // BAJA LOGICA
    public void eliminar(Long id) {
        // Valida si existe la Ubicacion por id
        Ubicacion u = ubicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Ubicacion con el ID = "+id));

        // Valida si la Ubicacion ya estaba inactiva
        ubicacionValidation.esInactivo(u);

        // Elimina logicamente la Ubicacion
        u.setActivo(false);

        // SE ACTUALIZA REALIZANDO SU BAJA LOGICA
        ubicacionRepository.save(u);
    }

}
