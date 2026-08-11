package pet_finder.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.mensaje.ConversacionDetailDTO;
import pet_finder.dtos.mensaje.MensajeDetailDTO;
import pet_finder.dtos.mensaje.MensajeRequestDTO;
import pet_finder.services.MensajeService;

import java.util.List;

@RestController
@RequestMapping("/mensajes")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {
        this.mensajeService = mensajeService;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<MensajeDetailDTO> enviarMensaje(@Valid @RequestBody MensajeRequestDTO request, @AuthenticationPrincipal MiembroUserDetails userDetails) {
        Long idEmisor = userDetails.getId();
        MensajeDetailDTO enviado = mensajeService.enviarMensaje(request, idEmisor);
        return ResponseEntity.status(HttpStatus.CREATED).body(enviado);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/conversacion/{idMiembro}")
    public ResponseEntity<?> obtenerConversacion(@PathVariable Long idMiembro, @AuthenticationPrincipal MiembroUserDetails userDetails, @RequestParam(defaultValue = "0") Long desdeId) {
        Long idUsuario = userDetails.getId();
        List<MensajeDetailDTO> mensajes = mensajeService.obtenerConversacion(idUsuario, idMiembro, desdeId);

        return ResponseEntity.ok(mensajes);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/conversacion/{idMiembro}/leidos")
    public ResponseEntity<Void> marcarConversacionLeida(
            @PathVariable Long idMiembro,
            @AuthenticationPrincipal MiembroUserDetails userDetails) {

        mensajeService.marcarLeidos(userDetails.getId(), idMiembro);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/conversaciones")
    public ResponseEntity<?> listarConversaciones(@AuthenticationPrincipal MiembroUserDetails userDetails) {
        Long idUsuario = userDetails.getId();
        List<ConversacionDetailDTO> conversaciones = mensajeService.listarConversaciones(idUsuario);

        return ResponseEntity.ok(conversaciones);
    }
}
