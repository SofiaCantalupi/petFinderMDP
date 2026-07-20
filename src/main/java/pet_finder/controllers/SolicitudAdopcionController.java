package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.solicitud.ResolucionSolicitudRequestDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionDetailDTO;
import pet_finder.dtos.solicitud.SolicitudAdopcionRequestDTO;
import pet_finder.services.SolicitudAdopcionService;

import java.util.List;

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

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/recibidas")
    public ResponseEntity<List<SolicitudAdopcionDetailDTO>> listarRecibidas(@AuthenticationPrincipal MiembroUserDetails userDetails){
        List<SolicitudAdopcionDetailDTO> dtos = solicitudService.listarRecibidas(userDetails.getId());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/enviadas")
    public ResponseEntity<List<SolicitudAdopcionDetailDTO>> listarEnviadas(@AuthenticationPrincipal MiembroUserDetails userDetails){
        List<SolicitudAdopcionDetailDTO> dtos =  solicitudService.listarEnviadas(userDetails.getId());
        return  ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/estado/{id}")
    public ResponseEntity<SolicitudAdopcionDetailDTO> resolver(@PathVariable Long id,
                                                                                @AuthenticationPrincipal MiembroUserDetails userDetails,
                                                                                @Valid @RequestBody ResolucionSolicitudRequestDTO requestDTO){
        SolicitudAdopcionDetailDTO resuelta = solicitudService.resolverSolicitudAdopcion(id,userDetails.getId(),requestDTO);
        return ResponseEntity.ok(resuelta);
    }
}
