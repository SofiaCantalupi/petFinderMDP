package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.dtos.mascota.MascotaRequestUpdateDTO;
import pet_finder.models.Mascota;
import pet_finder.repositories.MascotaRepository;
import pet_finder.validations.MascotaValidation;
import pet_finder.validations.MiembroValidation;

import java.util.List;

@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;

    private final MascotaValidation mascotaValidation;
    private final MiembroValidation miembroValidation;

    public MascotaService(MascotaRepository mascotaRepository, MascotaValidation mascotaValidation, MiembroValidation miembroValidation) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaValidation = mascotaValidation;
        this.miembroValidation = miembroValidation;
    }

    /*  obtenerPorId y listar devuelven solo registros activos, es decir, sin baja logica*/


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

    public Mascota modificar(Long mascotaId, Long miembroId, MascotaRequestUpdateDTO request){
        // Se obtiene la mascota que se quiere modificar, ademas se valida que la mascota exista y este activa
        Mascota existente = obtenerPorId(mascotaId);

        // Se valida que la mascota pertenezca al miembro que esta logeado y tratando de modificar el registro
        miembroValidation.estaLogeado(existente.getMiembroId(), miembroId);

        // Se valida que al menos haya un campo para modificar
        if (request.getNombre() == null &&
                request.getEstadoMascota() == null &&
                request.getTipoMascota() == null &&
                request.getFotoUrl() == null) {
            throw new IllegalArgumentException("Debe proporcionar al menos un campo para modificar.");
        }

        // Actualiza el campo solo si no es null
        if(request.getNombre() != null){
            existente.setNombre(request.getNombre());
        }

        if(request.getEstadoMascota() != null){
            existente.setEstadoMascota(request.getEstadoMascota());
        }

        if(request.getTipoMascota() != null){
            existente.setTipoMascota(request.getTipoMascota());
        }

        if(request.getFotoUrl() != null){
            existente.setFotoUrl(request.getFotoUrl());
        }

        return mascotaRepository.save(existente);
    }

    // Solo retorna las mascotas con esActivo = true
    public List<Mascota> listar(){
        return mascotaRepository.findAllByEsActivoTrue();
    }
}
