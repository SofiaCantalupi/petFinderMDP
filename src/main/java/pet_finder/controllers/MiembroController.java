package pet_finder.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import pet_finder.config.MiembroUserDetails;
import pet_finder.dtos.miembro.MiembroDetailDTO;
import pet_finder.dtos.miembro.MiembroRequestDTO;
import pet_finder.dtos.miembro.MiembroRequestUpdateDTO;
import pet_finder.services.MiembroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/miembros")
public class MiembroController {

    public final MiembroService miembroService;

    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<MiembroDetailDTO>> listar() {

        return ResponseEntity.ok(miembroService.listar());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<MiembroDetailDTO> obtenerPorId(@PathVariable Long id) {

        return ResponseEntity.ok(miembroService.obtenerDetallePorId(id));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping
    public ResponseEntity<MiembroDetailDTO> crear(@Valid @RequestBody MiembroRequestDTO request) {

        MiembroDetailDTO miembroCreado = miembroService.crear(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(miembroCreado);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @PutMapping("/modificar-datos")
    public ResponseEntity<MiembroDetailDTO> modificar(@Valid @RequestBody MiembroRequestUpdateDTO request,
            @AuthenticationPrincipal MiembroUserDetails userDetails) {

        // Se asegura de que el miembro que se va a modificar sea el autenticado por su
        // ID.
        return ResponseEntity.ok(miembroService.modificarDatos(request, userDetails.getId()));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/hacer-administrador/{id}")
    public ResponseEntity<String> hacerAdministradorPorId(@PathVariable Long id) {

        MiembroDetailDTO nuevoAdmin = miembroService.hacerAdministrador(id);

        return ResponseEntity.ok("El miembro " + nuevoAdmin.nombre() + " " + nuevoAdmin.apellido()
                + " es ahora administrador en el sistema.");
    }

    // Por ID elimina el administrador (ya que seria el que sabe los IDS de los
    // miembros)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(miembroService.eliminarPorId(id));
    }

    @PreAuthorize("hasAnyRole('MIEMBRO', 'ADMINISTRADOR')")
    @DeleteMapping("/cuenta")
    public ResponseEntity<String> eliminarCuentaPropia(@AuthenticationPrincipal MiembroUserDetails userDetails) {

        miembroService.eliminarPorId(userDetails.getId());

        return ResponseEntity.ok("Tu cuenta, tus publicaciones y tus comentarios fueron dados de baja con éxito.");
    }

}
