package pet_finder.controllers;


import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.mascota.MascotaRequestDTO;
import pet_finder.dtos.mascota.MascotaRequestUpdateDTO;
import pet_finder.models.Publicacion;
import pet_finder.services.MascotaService;
import pet_finder.services.PublicacionService;
import java.util.Optional;


import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    public final MascotaService service;
    public final PublicacionService publicacionService;

    public MascotaController(MascotaService service, PublicacionService publicacionService) {
        this.service = service;
        this.publicacionService = publicacionService;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/id/{id}")
    public ResponseEntity<MascotaDetailDTO> obtenerPorId(@PathVariable Long id) {

        return ResponseEntity.ok(service.obtenerDetallePorId(id));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<MascotaDetailDTO> crear(@Valid @RequestBody MascotaRequestDTO request,
                                                  @AuthenticationPrincipal MiembroUserDetails userDetails) {

        Long miembroID = userDetails.getId(); // se obtiene el id del miembro loggeado

        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(request, miembroID));

    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/{id}")
    public ResponseEntity<MascotaDetailDTO> modificar(@Valid @RequestBody MascotaRequestUpdateDTO request,
                                                      @PathVariable Long id,
                                                      @AuthenticationPrincipal MiembroUserDetails userDetails) {


        return ResponseEntity.ok(service.modificar(id, userDetails.getId(), request));
    }

@PreAuthorize("hasRole('MIEMBRO')")
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable Long id,
                                      @AuthenticationPrincipal MiembroUserDetails userDetails) {

    Optional<Publicacion> publicacionAsociada = publicacionService.buscarPorMascotaId(id);

    if (publicacionAsociada.isPresent()) {
        // Cascada completa: mascota, ubicación, comentarios y solicitudes pendientes
        // (rechazo automático + notificación).
        publicacionService.eliminarPublicacionPropia(publicacionAsociada.get(), userDetails.getId());
    } else {
        // Mascota que nunca llegó a tener una publicación asociada.
service.eliminar(id, userDetails.getId());
    }

    return ResponseEntity.noContent().build();
}

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping
    public ResponseEntity<List<MascotaDetailDTO>> listar() {

        List<MascotaDetailDTO> details = service.listar();  //Acá se asegura que sean las activas.
        
        return ResponseEntity.ok(details);
    }

}
