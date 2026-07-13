package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.services.SolicitudAdopcionService;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudAdopcionController {
    public final SolicitudAdopcionService solicitudService;

    public SolicitudAdopcionController(SolicitudAdopcionService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<SolicitudAdopcionDetailDTO> crear(@Valid @RequestBody SolicitudAdopcionRequestDTO request, @AuthenticationPrincipal MiembroUserDetails userDetails){
        Long miembroId = userDetails.getId();

        SolicitudAdopcionDetailDTO guardada = solicitudService.guardar(request,miembroId);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }
}
