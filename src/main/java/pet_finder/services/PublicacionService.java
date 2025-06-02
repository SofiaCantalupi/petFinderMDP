package pet_finder.services;

import pet_finder.dtos.PublicacionDetailDTO;
import pet_finder.dtos.PublicacionRequestDTO;
import pet_finder.models.Publicacion;
import pet_finder.models.Ubicacion;
import pet_finder.repositories.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.repositories.UbicacionRepository;

import java.util.List;

@Service
public class PublicacionService {

    private PublicacionRepository publicacionRepository;
    private MascotaRepository mascotaRepository;
    private MiembroRepository miembroRepository;
    private UbicacionRepository ubicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository,
                              MascotaRepository mascotaRepository,
                              MiembroRepository miembroRepository,
                              UbicacionRepository ubicacionRepository) {
        this.publicacionRepository = publicacionRepository;
        this.mascotaRepository = mascotaRepository;
        this.miembroRepository = miembroRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    // CREAR
    public PublicacionDetailDTO crearPublicacion (PublicacionRequestDTO req) {
        // Busca la Mascota segun el ID
        Mascota mascota = mascotaRepository.findById(req.mascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.mascotaId()));
        // Busca el Miembro segun el ID
        Miembro miembro = miembroRepository.findById(req.miembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.miembroId()));

        Publicacion p = new Publicacion();
        p.setDescripcion(req.descripcion());
        p.setMascota(mascota);
        p.setMiembro(miembro);
        // Crea una nueva Ubicacion asociada y lo setea en la Publicacion
        Ubicacion ubicacion = new Ubicacion(req.ubicacion().ciudad(),req.ubicacion().region(),req.ubicacion().pais());
        p.setUbicacion(ubicacion);
        // SE GUARDA LA PUBLICACION
        Publicacion save = publicacionRepository.save(p);

        return new PublicacionDetailDTO(save.getId(), save.getDescripcion(), save.getFecha(), save.getActivo(),
                save.getMascota().getId(), save.getMascota().getNombre(),
                save.getMiembro().getId(), save.getMiembro().getNombre(),
                save.getUbicacion().getCiudad(), save.getUbicacion().getRegion(), save.getUbicacion().getPais());

    }

    // LISTAR POR ID
    public PublicacionDetailDTO listarPublicacionPorId (Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        return new PublicacionDetailDTO(p.getId(), p.getDescripcion(), p.getFecha(), p.getActivo(),
                p.getMascota().getId(), p.getMascota().getNombre(),
                p.getMiembro().getId(), p.getMiembro().getNombre(),
                p.getUbicacion().getCiudad(), p.getUbicacion().getRegion(), p.getUbicacion().getPais());
    }

    // LISTAR
    public List<PublicacionDetailDTO> listarPublicaciones () {
        return publicacionRepository.findAll().stream()
                .map(p -> new PublicacionDetailDTO(p.getId(),p.getDescripcion(),p.getFecha(),p.getActivo(),p.getMascota().getId(),p.getMascota().getNombre(),p.getMiembro().getId(),p.getMiembro().getNombre(),p.getUbicacion().getId(),p.getUbicacion().getDireccion()))
                        .toList();
    }

    // ACTUALIZAR
    public PublicacionDetailDTO actualizarPublicacion (Long id, PublicacionRequestDTO req) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        p.setDescripcion(req.descripcion());

        Mascota mascota = mascotaRepository.findById(req.mascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.mascotaId()));
        p.setMascota(mascota);

        Miembro miembro = miembroRepository.findById(req.miembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.miembroId()));;
        p.setMiembro(miembro);

        Ubicacion ubicacion = new Ubicacion(req.ubicacion().ciudad(),req.ubicacion().region(),req.ubicacion().pais());
        p.setUbicacion(ubicacion);

        Publicacion update = publicacionRepository.save(p);

        return new PublicacionDetailDTO(update.getId(), update.getDescripcion(), update.getFecha(), update.getActivo(),
                update.getMascota().getId(), update.getMascota().getNombre(),
                update.getMiembro().getId(), update.getMiembro().getNombre(),
                update.getUbicacion().getCiudad(), update.getUbicacion().getRegion(), update.getUbicacion().getPais());
    }

    // BAJA LOGICA
    public void eliminarPublicacion (Long id) {
        // Eliminacion regular
        //if (!publicacionRepository.existsById(id)) {
        //      throw new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id);
        //}
        //publicacionRepository.deleteById(id);

        // Eliminacion logica de la Publicacion
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));
        p.setActivo(false);

        // Baja lógica de la ubicación asociada
        if (p.getUbicacion() != null) {
            p.getUbicacion().setActivo(false);
        }
        // SE ACTUALIZA LA PUBLICACION REALIZANDO SU BAJA (activo = false)
        publicacionRepository.save(p);
    }

}
