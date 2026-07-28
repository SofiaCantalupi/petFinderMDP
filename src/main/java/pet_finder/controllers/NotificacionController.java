package pet_finder.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.notificacion.NotificacionDetailDTO;
import pet_finder.mappers.NotificacionMapper;
import pet_finder.models.Notificacion;
import pet_finder.services.NotificacionService;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final NotificacionMapper notificacionMapper;

    public NotificacionController(NotificacionService notificacionService,
                                  NotificacionMapper notificacionMapper) {
        this.notificacionService = notificacionService;
        this.notificacionMapper = notificacionMapper;
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<NotificacionDetailDTO>> listarPropias(
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        List<Notificacion> notificaciones =
                notificacionService.listarPropias(miembroUserDetails.getId());

        return ResponseEntity.ok(
                notificacionMapper.deEntidadesAdetails(notificaciones)
        );
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @GetMapping("/no-leidas/cantidad")
    public ResponseEntity<Long> contarNoLeidas(
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        long cantidad =
                notificacionService.contarNoLeidas(miembroUserDetails.getId());

        return ResponseEntity.ok(cantidad);
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @PutMapping("/{id}/leida")
    public ResponseEntity<NotificacionDetailDTO> marcarComoLeida(
            @PathVariable Long id,
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        Notificacion notificacion =
                notificacionService.marcarComoLeida(id, miembroUserDetails.getId());

        return ResponseEntity.ok(
                notificacionMapper.aDetail(notificacion)
        );
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @PutMapping("/leidas")
    public ResponseEntity<String> marcarTodasComoLeidas(
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        notificacionService.marcarTodasComoLeidas(miembroUserDetails.getId());

        return ResponseEntity.ok("Todas las notificaciones fueron marcadas como leídas.");
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(
            @PathVariable Long id,
            @AuthenticationPrincipal MiembroUserDetails miembroUserDetails) {

        notificacionService.eliminarNotificacion(id, miembroUserDetails.getId());

        return ResponseEntity.ok("Notificación eliminada correctamente.");
    }
}
