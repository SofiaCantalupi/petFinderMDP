package pet_finder.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.dtos.MiembroRequestDTO;
import pet_finder.mappers.MiembroMapper;
import pet_finder.models.Miembro;
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
    public final MiembroMapper miembroMapper;

    public MiembroController(MiembroService miembroService,MiembroMapper miembroMapper) {
        this.miembroService = miembroService;
        this.miembroMapper = miembroMapper;
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping
    public ResponseEntity<List<MiembroDetailDTO>> listar(){
        return ResponseEntity.ok(miembroService.listar());
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @GetMapping("{id}")
    public ResponseEntity<MiembroDetailDTO> obtenerPorId(@PathVariable Long id){
        MiembroDetailDTO miembroDetailDTO = new MiembroDetailDTO(miembroService.obtenerPorId(id));
        return ResponseEntity.ok(miembroDetailDTO);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @PostMapping
    public ResponseEntity<MiembroDetailDTO> crear(@Valid @RequestBody MiembroRequestDTO request){
        Miembro miembro = miembroMapper.aEntidad(request);
        Miembro miembroCreado = miembroService.crear(miembro);
        return ResponseEntity.status(HttpStatus.CREATED).body(miembroMapper.aDetail(miembroCreado));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'MIEMBRO')")
    @PutMapping("/{id}")
    public ResponseEntity<MiembroDetailDTO> modificarPorId(@PathVariable Long id, @Valid @RequestBody MiembroRequestDTO request){
        Miembro miembro = miembroService.modificarPorId(id,request);
        MiembroDetailDTO nuevoMiembro = new MiembroDetailDTO(miembro);
        return ResponseEntity.ok(nuevoMiembro);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/hacer-administrador/{id}")
    public ResponseEntity<String> hacerAdministradorPorId(@PathVariable Long id){
        MiembroDetailDTO nuevoAdmin = new MiembroDetailDTO(miembroService.hacerAdministrador(id));
        return ResponseEntity.ok("El miembro " + nuevoAdmin.nombre() + " " + nuevoAdmin.apellido() + " es ahora administrador en el sistema.");
    }

    //Por ID elimina el administrador (ya que seria el que sabe los IDS de los miembros)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorId(@PathVariable Long id){
        miembroService.eliminarPorId(id);
        return ResponseEntity.ok("Miembro eliminado éxitosamente");
    }

    //Por mail borraría su cuenta el miembro, ya que el sabría su correo electronico, no su ID.
    @PreAuthorize("hasRole('MIEMBRO')")
    @DeleteMapping("/borrarPorEmail/{email}")
    public ResponseEntity<String> eliminarPorEmail(@PathVariable String email){
        miembroService.eliminarPorEmail(email);
        return ResponseEntity.ok("Miembro eliminado éxitosamente");
    }


}
