package pet_finder.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.models.Mascota;
import pet_finder.repositories.MascotaRepository;

import java.util.List;

@Service
public class MascotaService {
    private final MascotaRepository mascotaRepository;

    public MascotaService(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    /*
       CREAR de Request -> Mascota -> Detail

       ACTUALIZAR de Request -> Mascota -> Detail

       LISTAR de List<Entidad> -> List<Detail>

       OBTENER de Mascota -> Detail
     */

    public Mascota obtenerPorId(Long id){
        return mascotaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No se encontró una mascota con el ID: " + id));
    }

    public Mascota guardar(Mascota mascota){
        return mascotaRepository.save(mascota);
    }

    // Usar modificar en caso de que haya que validar algo en la actualizacion NADA MAS, los setters se usan en el mapper, la capa servicio no deberia conocer los dtos,
    // solo los conoce el Controller
    /*
    public Mascota modificar(Long id, Mascota mascota){

        return mascota;
    }

     */
    public void eliminar(Long id){
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No se encontró una mascota con el ID: " + id));

        // Baja pasiva
        mascota.setActivo(false);

        mascotaRepository.save(mascota);
    }

    public List<Mascota> listar(){
        return mascotaRepository.findAll();
    }
}
