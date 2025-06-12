package pet_finder.controllers;

import pet_finder.dtos.MiembroDetailDTO;
import pet_finder.dtos.MiembroRequestDTO;
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

    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    @GetMapping
    public ResponseEntity<List<MiembroDetailDTO>> listar(){
        return ResponseEntity.ok(miembroService.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<MiembroDetailDTO> obtenerPorId(@PathVariable Long id){
        MiembroDetailDTO miembroDetailDTO = new MiembroDetailDTO(miembroService.obtenerPorId(id));
        return ResponseEntity.ok(miembroDetailDTO);
    }

    @PostMapping        //Quizá se podria agregar la ruta /registro, porqué este vendria a ser el register.
    public ResponseEntity<MiembroDetailDTO> crear(@Valid @RequestBody MiembroRequestDTO request){
        MiembroDetailDTO nuevoMiembro = new MiembroDetailDTO(miembroService.crear(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMiembro);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MiembroDetailDTO> modificarPorId(@PathVariable Long id, @Valid @RequestBody MiembroRequestDTO request){
        Miembro miembro = miembroService.modificarPorId(id,request);
        MiembroDetailDTO nuevoMiembro = new MiembroDetailDTO(miembro);
        return ResponseEntity.ok(nuevoMiembro);
    }

    @PutMapping("/hacer-administrador/{id}")
    public ResponseEntity<String> hacerAdministradorPorId(@PathVariable Long id){
        MiembroDetailDTO nuevoAdmin = new MiembroDetailDTO(miembroService.hacerAdministrador(id));
        return ResponseEntity.ok("El miembro " + nuevoAdmin.nombre() + " " + nuevoAdmin.apellido() + " es ahora administrador en el sistema.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPorId(@PathVariable Long id){
        miembroService.eliminarPorId(id);
        return ResponseEntity.ok("Miembro eliminado éxitosamente");
    }

    @DeleteMapping("/borrarPorEmail/{email}")
    public ResponseEntity<String> eliminarPorEmail(@PathVariable String email){
        miembroService.eliminarPorEmail(email);
        return ResponseEntity.ok("Miembro eliminado éxitosamente");
    }


}
