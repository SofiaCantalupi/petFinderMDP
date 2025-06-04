package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.PublicacionDetailDTO;
import pet_finder.dtos.PublicacionRequestDTO;
import pet_finder.services.PublicacionService;

import java.util.List;

/**
 * @author Daniel Herrera
 */

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private PublicacionService publicacionService;

    public PublicacionController (PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<PublicacionDetailDTO> crearPublicacion (@Valid @RequestBody PublicacionRequestDTO req) {
        PublicacionDetailDTO dto = publicacionService.crearPublicacion(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDetailDTO>> listarPublicaciones () {
        List<PublicacionDetailDTO> publicaciones = publicacionService.listarPublicaciones();
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> listarPublicacionPorId (@PathVariable Long id) {
        PublicacionDetailDTO dto = publicacionService.listarPublicacionPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> actualizarPublicacion (@PathVariable Long id, @Valid @RequestBody PublicacionRequestDTO req) {
        PublicacionDetailDTO dto = publicacionService.actualizarPublicacion(id,req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPublicacion (@PathVariable Long id) {
        publicacionService.eliminarPublicacion(id);
        return ResponseEntity.noContent().build();
    }

}
