package pet_finder.controllers;

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

    @GetMapping("/miembros")
    public ResponseEntity<List<Miembro>> listarMiembros(){
        return ResponseEntity.ok(miembroService.listarMiembros());
    }

    @GetMapping("/miembros/{id}")
    public ResponseEntity<Miembro> buscarMiembroPorId(@PathVariable Long id){
        return ResponseEntity.ok(miembroService.obtenerMiembroPorId(id));
    }

    @PostMapping        //Quizá se podria agregar la ruta /registro, porqué este vendria a ser el register.
    public ResponseEntity<Miembro> altaMiembro(@Valid @RequestBody Miembro miembro){
        Miembro nuevoMiembro = miembroService.altaMiembro(miembro);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMiembro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> bajaMiembro(@PathVariable Long id){
        miembroService.borrarMiembroPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Miembro eliminado éxitosamente");
    }


}
