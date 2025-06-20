package pet_finder.controllers;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.mappers.ComentarioMapper;
import pet_finder.models.Comentario;
import pet_finder.services.ComentarioServices;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioServices comentarioService;
    private final ComentarioMapper comentarioMapper;

    public ComentarioController(ComentarioServices comentarioService, ComentarioMapper comentarioMapper) {
        this.comentarioService = comentarioService;
        this.comentarioMapper = comentarioMapper;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<ComentarioDetailDTO> crearComentario(@Valid @RequestBody ComentarioRequestDTO request, @AuthenticationPrincipal MiembroUserDetails userDetails) {

        //Se guarda el ID del miembro autenticado.
        Long idMiembro = userDetails.getId();

        Comentario comentario = comentarioMapper.aEntidad(request);

        Comentario creado = comentarioService.crearComentario(comentario, request.getIdPublicacion(), idMiembro);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ComentarioDetailDTO(creado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<?> listarPorPublicacion(@PathVariable Long idPublicacion) {

        List<Comentario> comentarios = comentarioService.listarPorPublicacion(idPublicacion);

        if (comentarios.isEmpty()) {
            return ResponseEntity.ok("Sé el primero en comentar.");
        }

        List<ComentarioDetailDTO> dtos = comentarioMapper.deEntidadesAdetails(comentarios);

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
    public ResponseEntity<String> eliminarComentarioPropio(@PathVariable Long id,@AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        //Se pide el ID del miembro autenticado para verificar que el comentario que se busca
        //borrar es propio del miembro.
        comentarioService.eliminarComentarioPropio(id, miembroUserDetails.getId());

        return ResponseEntity.ok("Comentario eliminado correctamente.");
    }
}
