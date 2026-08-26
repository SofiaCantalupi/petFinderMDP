package pet_finder.controllers;

import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.comentario.ComentarioDetailDTO;
import pet_finder.dtos.comentario.ComentarioRequestDTO;
import pet_finder.services.ComentarioService;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<ComentarioDetailDTO> crearComentario(@Valid @RequestBody ComentarioRequestDTO request,
            @AuthenticationPrincipal MiembroUserDetails userDetails) {

        // Se guarda el ID del miembro autenticado.
        Long idMiembro = userDetails.getId();

        ComentarioDetailDTO creado = comentarioService.crearComentario(request, idMiembro);

        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioDetailDTO>> listarPorPublicacion(@PathVariable Long idPublicacion) {

        List<ComentarioDetailDTO> dtos = comentarioService.listarDetallesPorPublicacion(idPublicacion);

        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Long id) {

        comentarioService.eliminarComentarioPorId(id);

        return ResponseEntity.ok("Se elimino correctamente");
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @DeleteMapping("/propio/{id}")
    public ResponseEntity<String> eliminarComentarioPropio(@PathVariable Long id,
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        // Se pide el ID del miembro autenticado para verificar que el comentario que se
        // busca
        // borrar es propio del miembro.
        comentarioService.eliminarComentarioPropio(id, miembroUserDetails.getId());

        return ResponseEntity.ok("Comentario eliminado correctamente.");
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @GetMapping("/{id}")
    public ResponseEntity<ComentarioDetailDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(comentarioService.obtenerDetallePorId(id));
    }
}
