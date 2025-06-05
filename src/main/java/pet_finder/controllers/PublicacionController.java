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
    public ResponseEntity<PublicacionDetailDTO> crear (@Valid @RequestBody PublicacionRequestDTO req) {
        PublicacionDetailDTO dto = publicacionService.crear(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<PublicacionDetailDTO>> listarActivas() {
        List<PublicacionDetailDTO> publicaciones = publicacionService.listarActivas();
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> listarPorId (@PathVariable Long id) {
        PublicacionDetailDTO dto = publicacionService.listarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> actualizar (@PathVariable Long id, @Valid @RequestBody PublicacionRequestDTO req) {
        PublicacionDetailDTO dto = publicacionService.actualizar(id,req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
