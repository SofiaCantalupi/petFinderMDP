package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.PublicacionDetailDTO;
import pet_finder.dtos.PublicacionRequestDTO;
import pet_finder.mappers.PublicacionMapper;
import pet_finder.models.Publicacion;
import pet_finder.services.PublicacionService;

import java.util.List;

/**
 * @author Daniel Herrera
 */

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private PublicacionService publicacionService;

    private PublicacionMapper publicacionMapper;

    public PublicacionController (PublicacionService publicacionService,
                                  PublicacionMapper publicacionMapper) {
        this.publicacionService = publicacionService;
        this.publicacionMapper = publicacionMapper;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<PublicacionDetailDTO> crear (@Valid @RequestBody PublicacionRequestDTO req) {
        Publicacion publicacion = publicacionMapper.aEntidad(req);
        Publicacion guardada = publicacionService.guardar(publicacion);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionMapper.aDetail(guardada));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<PublicacionDetailDTO>> listarActivas() {
        List<Publicacion> publicaciones = publicacionService.listarActivas();
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Transforma la List<Publicacion> en un ResponseEntity de List<PublicacionDetailDTO>
        return ResponseEntity.ok(publicacionMapper.deEntidadesAdetails(publicaciones));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> listarPorId (@PathVariable Long id) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.ok(publicacionMapper.aDetail(publicacion));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> modificar (@PathVariable Long id, @Valid @RequestBody PublicacionRequestDTO req) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);
        Publicacion modificado = publicacionMapper.modificar(publicacion,req);
        Publicacion actualizado = publicacionService.guardar(modificado);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.ok(publicacionMapper.aDetail(actualizado));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
