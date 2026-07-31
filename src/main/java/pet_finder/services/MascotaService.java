package pet_finder.services;

import org.springframework.stereotype.Service;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.mascota.MascotaRequestDTO;
import pet_finder.dtos.mascota.MascotaRequestUpdateDTO;
import pet_finder.mappers.MascotaMapper;
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

    private final MascotaMapper mascotaMapper;

    public MascotaService(MascotaRepository mascotaRepository, MascotaValidation mascotaValidation, MiembroValidation miembroValidation, MascotaMapper mascotaMapper) {
        this.mascotaRepository = mascotaRepository;
        this.mascotaValidation = mascotaValidation;
        this.miembroValidation = miembroValidation;
        this.mascotaMapper = mascotaMapper;
    }

    /*  obtenerPorId y listar devuelven solo registros activos, es decir, sin baja logica*/


    public Mascota obtenerPorId(Long id){

        // el metodo existePorId retorna la mascota encontrada por el id, de lo contrario lanza una excepcion
        Mascota existente = mascotaValidation.existePorId(id);

        // Se valida que el estado de la mascota que se quiere obtener sea esActivo = true, si es false, no puede obtenerse
        mascotaValidation.esActivo(existente.getEsActivo());

        return existente;
    }

    public MascotaDetailDTO obtenerDetallePorId(Long id){
        return mascotaMapper.aDetail(obtenerPorId(id));
    }

    public MascotaDetailDTO guardar(MascotaRequestDTO request, Long miembroId){
        Mascota mascota = mascotaMapper.aEntidad(request);
        mascota.setMiembroId(miembroId);

        Mascota guardada = mascotaRepository.save(mascota);
        return mascotaMapper.aDetail(guardada);
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

    public MascotaDetailDTO modificar(Long mascotaId, Long miembroId, MascotaRequestUpdateDTO request){
        // Se obtiene la mascota que se quiere modificar, ademas se valida que la mascota exista y este activa
        Mascota existente = obtenerPorId(mascotaId);

        // Se valida que la mascota pertenezca al miembro que esta logeado y tratando de modificar el registro
        miembroValidation.estaLogeado(existente.getMiembroId(), miembroId);

        // Se valida que al menos haya un campo para modificar
        if (request.getNombre() == null &&
                request.getTipoMascota() == null &&
                request.getUrlFoto() == null) {
            throw new IllegalArgumentException("Debe proporcionar al menos un campo para modificar.");
        }

        // Actualiza el campo solo si no es null
        if(request.getNombre() != null){
            existente.setNombre(request.getNombre());
        }

        if(request.getTipoMascota() != null){
            existente.setTipoMascota(request.getTipoMascota());
        }

        if(request.getUrlFoto() != null){
            existente.setUrlFoto(request.getUrlFoto());
        }

        Mascota guardada = mascotaRepository.save(existente);
        return mascotaMapper.aDetail(guardada);
    }

    // Solo retorna las mascotas con esActivo = true
    public List<MascotaDetailDTO> listar(){
        return mascotaMapper.deEntidadesAdetails(mascotaRepository.findAllByEsActivoTrue());
    }
}
