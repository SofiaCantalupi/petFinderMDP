package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.publicacion.PublicacionDetailDTO;
import pet_finder.dtos.publicacion.PublicacionRequestDTO;
import pet_finder.dtos.publicacion.PublicacionRequestUpdateDTO;
import pet_finder.models.Publicacion;
import pet_finder.services.PublicacionService;

import java.util.List;


@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController (PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<PublicacionDetailDTO> crear (@Valid @RequestBody PublicacionRequestDTO req,
                                                       @AuthenticationPrincipal MiembroUserDetails userDetails) {

        // Obtengo el id del miembro logeado, lo asocio a la publicación y lo relaciono a la publicacion
        Long miembroId = userDetails.getId();

        PublicacionDetailDTO guardada = publicacionService.guardar(req, miembroId);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> modificar(@PathVariable Long id,
                                                          @Valid @RequestBody PublicacionRequestUpdateDTO request,
                                                          @AuthenticationPrincipal MiembroUserDetails miembroUserDetails){

        return ResponseEntity.ok(publicacionService.modificar(id, miembroUserDetails.getId(), request));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/{id}/estado/{estado}")
    public ResponseEntity<PublicacionDetailDTO> modificarEstado(@PathVariable Long id,
                                                                @PathVariable String estado,
                                                                @AuthenticationPrincipal MiembroUserDetails miembroUserDetails){

        return ResponseEntity.ok(publicacionService.modificarEstado(id, miembroUserDetails.getId(), estado));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDetailDTO> obtenerPorId(@PathVariable Long id) {

        return ResponseEntity.ok(publicacionService.obtenerDetallePorId(id));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/propias")
    public ResponseEntity<List<PublicacionDetailDTO>> listarPropias(@AuthenticationPrincipal MiembroUserDetails userDetail){
        List<PublicacionDetailDTO> publicaciones = publicacionService.listarPropias(userDetail.getId());

        if(publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(publicaciones);
    }


    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<PublicacionDetailDTO>> listarActivas() {

        List<PublicacionDetailDTO> publicaciones = publicacionService.listarActivas();

        if (publicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(publicaciones);
    }


    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/tipoMascota/{tipoMascota}")
    public ResponseEntity<List<PublicacionDetailDTO>> filtrarPorTipoMascota(@PathVariable String tipoMascota){

        List<PublicacionDetailDTO> publicaciones = publicacionService.filtrarPorTipoMascota(tipoMascota);

        if(publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(publicaciones);
    }


    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/estadoMascota/{estadoMascota}")
    public ResponseEntity<List<PublicacionDetailDTO>> filtrarPorEstadoMascota(@PathVariable String estadoMascota){

        // FiltrarPorEstadoMascota se encarga de validar el parametro recibido y retornar una lista segun el enum
        List<PublicacionDetailDTO> publicaciones = publicacionService.filtrarPorEstadoMascota(estadoMascota);

        if (publicaciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(publicaciones);
    }

    // Ejemplo: GET http://localhost:8080/publicaciones/filtro?tipoMascota=PERRO&estadoMascota=PERDIDA
    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/filtro")
    public ResponseEntity<List<PublicacionDetailDTO>> filtrarPorTipoYEstado(
            @RequestParam String tipoMascota,
            @RequestParam String estadoMascota
    ) {
        List<PublicacionDetailDTO> filtradas = publicacionService.filtrarPorTipoYEstado(tipoMascota, estadoMascota);

        if (filtradas.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(filtradas);
    }


    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> eliminarPublicacionAdmin(@PathVariable Long id) {

        Publicacion publicacion = publicacionService.obtenerPorId(id);
        publicacionService.eliminar(publicacion);

        return ResponseEntity.ok("Publicación eliminada con éxito");
    }


    @PreAuthorize("hasRole('MIEMBRO')")
    @DeleteMapping("/propia/{id}")
    public  ResponseEntity<String> eliminarPublicacionPropia(@PathVariable Long id,@AuthenticationPrincipal MiembroUserDetails miembroUserDetails){

        Publicacion publicacion = publicacionService.obtenerPorId(id);
        publicacionService.eliminarPublicacionPropia(publicacion, miembroUserDetails.getId());

        return ResponseEntity.ok("Publicación eliminada con éxito");
    }
}
