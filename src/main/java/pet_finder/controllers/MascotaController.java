package pet_finder.controllers;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.mascota.MascotaDetailDTO;
import pet_finder.dtos.mascota.MascotaRequestDTO;
import pet_finder.dtos.mascota.MascotaRequestUpdateDTO;
import pet_finder.services.MascotaService;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    public final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
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

        return ResponseEntity.ok(service.guardar(request, miembroID));
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
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminar(id); // baja logica, no se elimina el registro

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping
    public ResponseEntity<List<MascotaDetailDTO>> listar() {

        List<MascotaDetailDTO> details = service.listar();  //Acá se asegura que sean las activas.

        if (details.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(details);
    }

}
