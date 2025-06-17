package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
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

    private final PublicacionService publicacionService;

    private final PublicacionMapper publicacionMapper;

    public PublicacionController (PublicacionService publicacionService,
                                  PublicacionMapper publicacionMapper) {
        this.publicacionService = publicacionService;
        this.publicacionMapper = publicacionMapper;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<PublicacionDetailDTO> crear (@Valid @RequestBody PublicacionRequestDTO req,
                                                       @AuthenticationPrincipal MiembroUserDetails userDetails) {

        Publicacion publicacion = publicacionMapper.aEntidad(req);

        // Obtengo el id del miembro loggeado y lo relaciono a la publicacion
        Long miembroId = userDetails.getId();
        publicacion.setIdMiembro(miembroId);

        Publicacion guardada = publicacionService.guardar(publicacion);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.status(HttpStatus.CREATED).body(publicacionMapper.aDetail(guardada));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<PublicacionDetailDTO>> listarActivas() {
        List<Publicacion> publicaciones = publicacionService.listarActivas();
        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Transforma la List<Publicacion> en un ResponseEntity de List<PublicacionDetailDTO>
        return ResponseEntity.ok(publicacionMapper.deEntidadesAdetails(publicaciones));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> obtenerPorId(@PathVariable Long id) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.ok(publicacionMapper.aDetail(publicacion));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> modificar (@PathVariable Long id, @Valid @RequestBody PublicacionRequestDTO req) {
        Publicacion publicacion = publicacionService.obtenerPorId(id);
        Publicacion modificado = publicacionMapper.modificar(publicacion,req);
        Publicacion actualizado = publicacionService.guardar(modificado);
        // Transforma la Publicacion en un ResponseEntity de PublicacionDetailDTO
        return ResponseEntity.ok(publicacionMapper.aDetail(actualizado));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar (@PathVariable Long id) {
        publicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/tipoMascota/{tipoMascota}")
    public ResponseEntity<List<PublicacionDetailDTO>> filtrarPorTipoMascota(@PathVariable String tipoMascota){
        List<Publicacion> publicaciones = publicacionService.filtrarPorTipoMascota(tipoMascota);

        if(publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        // Se mappean las publicaciones enontradas a detailsDTO
        return ResponseEntity.ok(publicacionMapper.deEntidadesAdetails(publicaciones));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/estadoMascota/{estadoMascota}")
    public ResponseEntity<List<PublicacionDetailDTO>> filtrarPorEstadoMascota(@PathVariable String estadoMascota){
        // FiltrarPorEstadoMascota se encarga de validar el parametro recibido y retornar una lista segun el enum
        List<Publicacion> publicaciones = publicacionService.filtrarPorEstadoMascota(estadoMascota);

        if (publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        // Se mappean las publicaciones enontradas a detailsDTO
        return ResponseEntity.ok(publicacionMapper.deEntidadesAdetails(publicaciones));
    }
}
