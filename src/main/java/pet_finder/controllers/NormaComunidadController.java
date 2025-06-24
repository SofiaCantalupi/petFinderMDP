package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.norma.NormaComunidadDetailDTO;
import pet_finder.dtos.norma.NormaComunidadRequestDTO;
import pet_finder.models.NormaComunidad;
import pet_finder.services.NormaComunidadService;

import java.util.List;

@RestController
@RequestMapping("/normas")
public class NormaComunidadController {
    private final NormaComunidadService service;

    public NormaComunidadController(NormaComunidadService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @GetMapping
    public ResponseEntity<List<NormaComunidadDetailDTO>> listar(){
        List<NormaComunidad> normas = service.verNormas();

        if(normas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        // Se mappea cada norma a un detailDTO
        List<NormaComunidadDetailDTO> details = normas
                .stream().map(NormaComunidadDetailDTO::new)
                .toList();

        return ResponseEntity.ok(details);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<NormaComunidadDetailDTO> crear(@Valid @RequestBody NormaComunidadRequestDTO request){
        // Se crea una norma y se le guarda el texto del requestDTO
        NormaComunidad norma = new NormaComunidad();
        norma.setTexto(request.getTexto());

        service.crear(norma);

        // Se retorna un detailDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(new NormaComunidadDetailDTO(norma));
    }
}
