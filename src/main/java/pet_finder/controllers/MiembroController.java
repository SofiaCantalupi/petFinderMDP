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
    public ResponseEntity<List<Miembro>> listar(){
        return ResponseEntity.ok(miembroService.listar());
    }

    @GetMapping("/miembros/{id}")
    public ResponseEntity<MiembroDetailDTO> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(miembroService.obtenerPorId(id));
    }

    @PostMapping        //Quizá se podria agregar la ruta /registro, porqué este vendria a ser el register.
    public ResponseEntity<MiembroDetailDTO> crear(@Valid @RequestBody MiembroRequestDTO request){
        MiembroDetailDTO nuevoMiembro = miembroService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMiembro);
    }
    @PutMapping("/{id}")
    public ResponseEntity<MiembroDetailDTO> modificarPorId(@PathVariable Long id, @Valid @RequestBody MiembroRequestDTO request){
        MiembroDetailDTO nuevoMiembro = miembroService.modificarPorId(id,request);
        return ResponseEntity.ok(nuevoMiembro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> hacerAdministradorPorId(@PathVariable Long id){
        MiembroDetailDTO nuevoAdmin = miembroService.hacerAdministrador(id);
        return ResponseEntity.ok("El miembro " + nuevoAdmin.nombre() + " " + nuevoAdmin.apellido() + " es ahora administrador en el sistema.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id){
        miembroService.eliminarPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Miembro eliminado éxitosamente");
    }


}
