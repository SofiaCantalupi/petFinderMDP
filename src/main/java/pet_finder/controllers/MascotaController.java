package pet_finder.controllers;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pet_finder.dtos.MascotaDetailDTO;
import pet_finder.dtos.MascotaRequestDTO;
import pet_finder.mappers.MascotaMapper;
import pet_finder.models.Mascota;
import pet_finder.services.MascotaService;

import java.util.List;

@RestController
@RequestMapping("api/mascotas")
public class MascotaController {
    public final MascotaService service;
    public final MascotaMapper mapper;


    public MascotaController(MascotaService service, MascotaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping("/id/{id}")
    public ResponseEntity<MascotaDetailDTO> obtenerPorId(@PathVariable Long id){
        Mascota encontrada = service.obtenerPorId(id);
        return ResponseEntity.ok(mapper.aDetail(encontrada));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PostMapping
    public ResponseEntity<MascotaDetailDTO> crear(@Valid @RequestBody MascotaRequestDTO request){
       Mascota mascota = mapper.aEntidad(request); // el request es mappeado a entidad
       Mascota guardada = service.guardar(mascota); // se guarda la mascota nueva
       return ResponseEntity.ok(mapper.aDetail(guardada));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @PutMapping("/mascota/{id}")
    public ResponseEntity<MascotaDetailDTO> modificar(@Valid @RequestBody MascotaRequestDTO request, @PathVariable Long id){
        Mascota existente = service.obtenerPorId(id);  // este metodo valida tanto existencia como activo
        Mascota modificada = mapper.modificar(existente,request); // toma la entidad que se quiere modificar y retorna una entidad con los cambios realizados
        Mascota guardada = service.guardar(modificada); // guarda la entidad con  los respectivos cambios

        return ResponseEntity.ok(mapper.aDetail(guardada));
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        service.eliminar(id); // baja logica, no se elimina el registro
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MIEMBRO')")
    @GetMapping
    public ResponseEntity<List<MascotaDetailDTO>> listar(){
        List<Mascota> mascotas = service.listar();
        List<MascotaDetailDTO> details = mapper.deEntidadesAdetails(mascotas);

        if(details.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(details);
    }

}
