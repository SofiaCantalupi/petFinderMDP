package pet_finder.controllers;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.mappers.ComentarioMapper;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.services.ComentarioServices;
import pet_finder.services.MiembroService;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ComentarioServices comentarioService;
    private final MiembroService miembroService;
    private final ComentarioMapper comentarioMapper;


    public ComentarioController(ComentarioServices comentarioService, MiembroService miembroService, ComentarioMapper comentarioMapper) {
        this.comentarioService = comentarioService;
        this.miembroService = miembroService;
        this.comentarioMapper = comentarioMapper;
    }


    @PostMapping
    public ResponseEntity<ComentarioDetailDTO> crearComentario(@Valid @RequestBody ComentarioRequestDTO request,
                                                               @AuthenticationPrincipal MiembroUserDetails userDetails) {

        Long miembroId = userDetails.getId(); // se obtiene el ID del miembro loggeado

        Comentario comentario = comentarioMapper.aEntidad(request);

        // Crear comentario, recibe el id del miembro y el id de la publicacion, los valida y los settea en la entidad
        Comentario creado = comentarioService.crearComentario(comentario, request.getIdPublicacion(), miembroId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ComentarioDetailDTO(creado));
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<?> listarPorPublicacion(@PathVariable Long idPublicacion) {
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(idPublicacion);
        if (comentarios.isEmpty()) {
            return ResponseEntity.ok("Sé el primero en comentar.");
        }

        List<ComentarioDetailDTO> dtos = comentarioMapper.deEntidadesAdetails(comentarios);

        return ResponseEntity.ok(dtos);
    }

    //esto hay que arreglarlo para que SOLO EL ADMIN pueda dar de baja los comentarios
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Long id) {
            comentarioService.eliminarComentarioPorId(id);
            return ResponseEntity.ok("Se elimino correctamente");
    }

    @DeleteMapping("/propio/{id}")
    public ResponseEntity<String> eliminarComentarioPropio(@PathVariable Long id) {
        // Obtener el email del usuario autenticado desde el SecurityContext
        String emailMiembro = SecurityContextHolder.getContext().getAuthentication().getName();

        // Ejecutar eliminación delegando al servicio
        comentarioService.eliminarComentarioPropio(id, emailMiembro);

        return ResponseEntity.ok("Comentario eliminado correctamente.");
    }
}
