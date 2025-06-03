package pet_finder.controllers;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.MascotaDetailDTO;
import pet_finder.dtos.MascotaRequestDTO;
import pet_finder.mappers.MascotaMapper;
import pet_finder.models.Mascota;
import pet_finder.services.MascotaService;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {
    public final MascotaService service;
    public final MascotaMapper mapper;


    public MascotaController(MascotaService service, MascotaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MascotaDetailDTO> obtenerPorId(@PathVariable Long id){
        Mascota encontrada = service.obtenerPorId(id);
        return ResponseEntity.ok(mapper.aDetail(encontrada));
    }

    @PostMapping
    public ResponseEntity<MascotaDetailDTO> crear(@Valid @RequestBody MascotaRequestDTO request){
       Mascota mascota = mapper.aEntidad(request);
       Mascota guardada = service.guardar(mascota);
       return ResponseEntity.ok(mapper.aDetail(guardada));
    }

    @PutMapping("/mascota/{id}")
    public ResponseEntity<MascotaDetailDTO> modificar(@Valid @RequestBody MascotaRequestDTO request, @PathVariable Long id){
        Mascota encontrada = service.obtenerPorId(id);
        Mascota modificada = mapper.modificar(encontrada,request);
        service.guardar(encontrada);

        return ResponseEntity.ok(mapper.aDetail(modificada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<MascotaDetailDTO>> listar(){
        List<Mascota> mascotas = service.listar();
        List<MascotaDetailDTO> details = mapper.deEntidadesAdetails(mascotas);

        if(details.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(details);
    }

}
