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
    public ResponseEntity<?> obtenerConversacion(@PathVariable Long idMiembro, @AuthenticationPrincipal MiembroUserDetails userDetails) {
        Long idUsuario = userDetails.getId();
        List<MensajeDetailDTO> mensajes = mensajeService.obtenerConversacion(idUsuario, idMiembro);

        if (mensajes.isEmpty()) {
            return ResponseEntity.ok("No hay mensajes en esta conversación.");
        }

        return ResponseEntity.ok(mensajes);
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/conversaciones")
    public ResponseEntity<?> listarConversaciones(@AuthenticationPrincipal MiembroUserDetails userDetails) {
        Long idUsuario = userDetails.getId();
        List<ConversacionDetailDTO> conversaciones = mensajeService.listarConversaciones(idUsuario);

        if (conversaciones.isEmpty()) {
            return ResponseEntity.ok("No tenés conversaciones aún.");
        }

        return ResponseEntity.ok(conversaciones);
    }
}
