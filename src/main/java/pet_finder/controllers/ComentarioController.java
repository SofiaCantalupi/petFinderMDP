package pet_finder.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.mappers.ComentarioMapper;
import pet_finder.models.Comentario;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;
import pet_finder.services.ComentarioServices;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {


    private final ComentarioServices comentarioService;
    private final ComentarioMapper comentarioMapper;
    private final MiembroRepository miembroRepository;
    private final PublicacionRepository publicacionRepository;

    public ComentarioController(ComentarioServices comentarioService, ComentarioMapper comentarioMapper, MiembroRepository miembroRepository, PublicacionRepository publicacionRepository) {
        this.comentarioService = comentarioService;
        this.comentarioMapper = comentarioMapper;
        this.miembroRepository = miembroRepository;
        this.publicacionRepository = publicacionRepository;
    }



    @PostMapping
    public ResponseEntity<ComentarioDetailDTO> crearComentario(@Valid @RequestBody ComentarioRequestDTO request) {

        Comentario comentario = comentarioMapper.aEntidad(request);

        Comentario creado = comentarioService.crearComentario(comentario, request.getIdPublicacion(), request.getIdUsuario());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ComentarioDetailDTO(creado));
    }



    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioDetailDTO>> listarPorPublicacion(@PathVariable Long idPublicacion) {
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(idPublicacion);
        List<ComentarioDetailDTO> dtos = comentarioMapper.deEntidadesAdetails(comentarios);


        return ResponseEntity.ok(dtos);
    }



    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentarioPorId(id);
        return ResponseEntity.ok("Se elimino correctamente");
    }





}
