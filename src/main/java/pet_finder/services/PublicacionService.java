package com.UTN.pet_finder_mdp.services;

import com.UTN.pet_finder_mdp.dtos.PublicacionDetailDTO;
import com.UTN.pet_finder_mdp.dtos.PublicacionRequestDTO;
import com.UTN.pet_finder_mdp.models.Publicacion;
import com.UTN.pet_finder_mdp.repositories.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public PublicacionDetailDTO crearPublicacion (PublicacionRequestDTO req) {

        Mascota mascota = mascotaRepository.findById(req.getMascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.getMascotaId()));

        Miembro miembro = miembroRepository.findById(req.getMiembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.getMiembroId()));;

        Ubicacion ubicacion = ubicacionRepository.findById(req.getUbicacionId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Ubicacion con el ID = "+req.getUbicacionId()));;

        Publicacion p = new Publicacion();
        p.setDescripcion(req.getDescripcion());
        p.setMascota(mascota);
        p.setMiembro(miembro);
        p.setUbicacion(ubicacion);
        Publicacion save = publicacionRepository.save(p);

        return new PublicacionDetailDTO(save.getId(), save.getDescripcion(), save.getFecha(), save.getActivo(),
                save.getMascota().getId(), save.getMascota().getNombre(),
                save.getMiembro().getId(), save.getMiembro().getNombre(),
                save.getUbicacion().getId(), save.getUbicacion().getDireccion());

    }

    public PublicacionDetailDTO listarPublicacionPorId (Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        return new PublicacionDetailDTO(save.getId(), save.getDescripcion(), save.getFecha(), save.getActivo(),
                save.getMascota().getId(), save.getMascota().getNombre(),
                save.getMiembro().getId(), save.getMiembro().getNombre(),
                save.getUbicacion().getId(), save.getUbicacion().getDireccion());
    }

    public List<PublicacionDetailDTO> listarPublicaciones () {
        return publicacionRepository.findAll().stream()
                .map(p -> new PublicacionDetailDTO(p.getId(),p.getDescripcion(),p.getFecha(),p.getActivo(),p.getMascota().getId(),p.getMascota().getNombre(),p.getMiembro().getId(),p.getMiembro().getNombre(),p.getUbicacion().getId(),p.getUbicacion().getDireccion()))
                        .toList();
    }

    public PublicacionDetailDTO actualizarPublicacion(Long id, PublicacionRequestDTO req) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        p.setDescripcion(req.getDescripcion());

        Mascota mascota = mascotaRepository.findById(req.getMascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.getMascotaId()));
        p.setMascota(mascota);

        Miembro miembro = miembroRepository.findById(req.getMiembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.getMiembroId()));;
        p.setMiembro(miembro);

        Ubicacion ubicacion = ubicacionRepository.findById(req.getUbicacionId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Ubicacion con el ID = "+req.getUbicacionId()));;
        p.setUbicacion(ubicacion);

        Publicacion update = publicacionRepository.save(p);

        return new PublicacionDetailDTO(update.getId(), update.getDescripcion(), update.getFecha(), update.getActivo(),
                update.getMascota().getId(), update.getMascota().getNombre(),
                update.getMiembro().getId(), update.getMiembro().getNombre(),
                update.getUbicacion().getId(), update.getUbicacion().getDireccion());
    }

    public void eliminarPublicacion(Long id) {
        // Eliminacion regular
        //if (!publicacionRepository.existsById(id)) {
        //      throw new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id);
        //}
        //publicacionRepository.deleteById(id);

        // Eliminacion logica
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));
        p.setActivo(false);
        publicacionRepository.save(p);
    }

}
