package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.ComentarioDetailDTO;
import pet_finder.dtos.ComentarioRequestDTO;
import pet_finder.mappers.ComentarioMapper;
import pet_finder.models.Comentario;
import pet_finder.services.ComentarioServices;

import java.util.List;

@Controller
@RestController("/comentarios")
public class ComentarioController {


    private final ComentarioServices comentarioService;
    private final ComentarioMapper comentarioMapper;


    public ComentarioController(ComentarioServices comentarioService, ComentarioMapper comentarioMapper) {
        this.comentarioService = comentarioService;
        this.comentarioMapper = comentarioMapper;
    }



    @PostMapping
    public ResponseEntity<ComentarioDetailDTO> crearComentario(@RequestBody @Valid ComentarioRequestDTO requestDTO) {

        //convierte el dto de request a entidad Comentario
        Comentario comentarioEntidad = comentarioMapper.aEntidad(requestDTO);


        Comentario comentarioGuardado = comentarioService.crearComentario(comentarioEntidad);

        // Convertir la entidad guardada a DTO detail para el retorno
        ComentarioDetailDTO detalleDTO = comentarioMapper.aDetail(comentarioGuardado);

        return ResponseEntity.ok(detalleDTO);
    }



    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioDetailDTO>> listarPorPublicacion(@PathVariable Long idPublicacion) {
        List<Comentario> comentarios = comentarioService.listarPorPublicacion(idPublicacion);
        List<ComentarioDetailDTO> dtos = comentarioMapper.deEntidadesAdetails(comentarios);


        return ResponseEntity.ok(dtos);
    }



    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentarioPorId(id);
        return ResponseEntity.noContent().build();
    }





}
