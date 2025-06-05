package pet_finder.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.models.Mascota;
import pet_finder.repositories.MascotaRepository;
import pet_finder.validations.MascotaValidation;

import java.util.List;

@Service
public class MascotaService {
    private final MascotaRepository mascotaRepository;
    private final MascotaValidation mascotaValidation;

    public MascotaService(MascotaRepository mascotaRepository, MascotaValidation mascotaValidation) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaValidation = mascotaValidation;
    }

    /* ---------Aclaracion sobre los metodos CRUD de esta clase --------------
    Con el objetivo de que los mappeos entre entidad y dto queden unicamente en el Controller, se crearon unicamente los metodos
    - obtenerPorId
    - guardar : no se llama crear, ya que el metodo guardar tambien es usado en el controller para modificar
    - eliminar
    - listar

    1.No se hace un metodo modificar como tal -> al modificar una mascota, se obtiene por id, validando que exista y este activa, se modifica a traves del mappeo y se guarda la mascota
    modificada en el controller, a traves del metodo guardar de este service.

    2. obtenerPorId y listar devuelven solo registros activos, es decir, sin baja logica

     */

    public Mascota obtenerPorId(Long id){
        // el metodo existePorId retorna la mascota encontrada por el id, de lo contrario lanza una excepcion
        Mascota existente = mascotaValidation.existePorId(id);

        // Se valida que el estado de la mascota que se quiere obtener sea esActivo = true, si es false, no puede obtenerse
        mascotaValidation.esActivo(existente.getEsActivo());

        return existente;
    }

    public Mascota guardar(Mascota mascota){
        return mascotaRepository.save(mascota);
    }

    public void eliminar(Long id){
        // Se valida que la mascota exista
        Mascota mascota = mascotaValidation.existePorId(id);

        // Se valida que el estado de la mascota que se quiere eliminar tengas su atributo esActivo == true
        mascotaValidation.esActivo(mascota.getEsActivo());

        // Baja pasiva
        mascota.setEsActivo(false);

        mascotaRepository.save(mascota);
    }

    // Solo retorna las mascotas con esActivo = true
    public List<Mascota> listar(){
        return mascotaRepository.findAllByEsActivoTrue();
    }
}
