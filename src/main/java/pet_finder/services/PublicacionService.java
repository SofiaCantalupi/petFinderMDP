package pet_finder.services;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pet_finder.dtos.PublicacionDetailDTO;
import pet_finder.dtos.PublicacionRequestDTO;
import pet_finder.dtos.UbicacionRequestDTO;
import pet_finder.models.Mascota;
import pet_finder.models.Miembro;
import pet_finder.models.Publicacion;
import pet_finder.models.Ubicacion;
import pet_finder.repositories.MascotaRepository;
import pet_finder.repositories.MiembroRepository;
import pet_finder.repositories.PublicacionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pet_finder.repositories.UbicacionRepository;

import java.util.List;

/**
 * @author Daniel Herrera
 */

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final MascotaRepository mascotaRepository;
    private final MiembroRepository miembroRepository;

    public PublicacionService(PublicacionRepository publicacionRepository,
                              MascotaRepository mascotaRepository,
                              MiembroRepository miembroRepository) {
        this.publicacionRepository = publicacionRepository;
        this.mascotaRepository = mascotaRepository;
        this.miembroRepository = miembroRepository;
    }

    // CREAR
    public PublicacionDetailDTO crearPublicacion (PublicacionRequestDTO req) {
        // todo: (CAMBIAR ESTO) Busca la Mascota segun el ID
        Mascota mascota = mascotaRepository.findById(req.mascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.mascotaId()));

        // Busca el Miembro segun el ID
        Miembro miembro = miembroRepository.findById(req.miembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.miembroId()));

        // Setea una nueva Publicacion (en el constructor se hace alta = true)
        Publicacion p = new Publicacion();
        p.setDescripcion(req.descripcion());
        p.setMascota(mascota);
        p.setMiembro(miembro);

        // Valida la Ubicacion enviada corroborando que sea verdadera
        // Lo hace mediante la funcion "validarUbicacion(ubicacionReqDTO)"
        boolean ubicacionReal = validarUbicacion(req.ubicacion());
        // SI NO retorno true entonces la ubicacion no se pudo geocodificar
        if (!ubicacionReal) {
            throw new IllegalArgumentException("La Ubicacion '"
                    +req.ubicacion().pais()+", "
                    +req.ubicacion().region()+", "
                    +req.ubicacion().ciudad()+"' no se encontró.");
        }
        // En caso de que la ubicacion sea verdadera y no lance excepcion
        // Se crea una nueva Ubicacion asociada (en el constructor alta = true)
        Ubicacion u = new Ubicacion(
                req.ubicacion().direccion(),
                req.ubicacion().altura(),
                req.ubicacion().ciudad(),
                req.ubicacion().region(),
                req.ubicacion().pais());
        // Setea la Ubicacion
        p.setUbicacion(u);

        // SE GUARDA LA PUBLICACION
        Publicacion save = publicacionRepository.save(p);

        return new PublicacionDetailDTO(save.getId(),
                save.getDescripcion(), save.getFecha(), save.getActivo(),
                save.getMascota().getNombre(), save.getMascota().getTipoMascota().name(),
                save.getMiembro().getId(), save.getMiembro().getNombre(),
                save.getUbicacion().getDireccion(), save.getUbicacion().getAltura(),
                save.getUbicacion().getCiudad(), save.getUbicacion().getRegion(),
                save.getUbicacion().getPais());
    }

    // LISTAR POR ID
    public PublicacionDetailDTO listarPublicacionPorId (Long id) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        return new PublicacionDetailDTO(p.getId(),
                p.getDescripcion(), p.getFecha(), p.getActivo(),
                p.getMascota().getNombre(), p.getMascota().getTipoMascota().name(),
                p.getMiembro().getId(), p.getMiembro().getNombre(),
                p.getUbicacion().getDireccion(), p.getUbicacion().getAltura(),
                p.getUbicacion().getCiudad(), p.getUbicacion().getRegion(),
                p.getUbicacion().getPais());
    }

    // LISTAR
    public List<PublicacionDetailDTO> listarPublicaciones () {
        return publicacionRepository.findAll().stream()
                .map(p -> new PublicacionDetailDTO(p.getId(),
                        p.getDescripcion(), p.getFecha(), p.getActivo(),
                        p.getMascota().getNombre(), p.getMascota().getTipoMascota().name(),
                        p.getMiembro().getId(), p.getMiembro().getNombre(),
                        p.getUbicacion().getDireccion(), p.getUbicacion().getAltura(),
                        p.getUbicacion().getCiudad(), p.getUbicacion().getRegion(),
                        p.getUbicacion().getPais()))
                        .toList();
    }

    // ACTUALIZAR
    public PublicacionDetailDTO actualizarPublicacion (Long id, PublicacionRequestDTO req) {
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));

        p.setDescripcion(req.descripcion());

        Mascota mascota = mascotaRepository.findById(req.mascotaId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Mascota con el ID = "+req.mascotaId()));
        p.setMascota(mascota);

        Miembro miembro = miembroRepository.findById(req.miembroId())
                .orElseThrow(() -> new EntityNotFoundException("No se encontro el Miembro con el ID = "+req.miembroId()));;
        p.setMiembro(miembro);

        // Recibe la ubicacion request DTO y crea una Ubicacion
        Ubicacion ubicacion = new Ubicacion(
                req.ubicacion().direccion(), req.ubicacion().altura(),
                req.ubicacion().ciudad(),req.ubicacion().region(),req.ubicacion().pais());
        // Setea la Ubicacion creada
        p.setUbicacion(ubicacion);

        // ACTUALIZA LA PUBLICACION
        Publicacion update = publicacionRepository.save(p);

        return new PublicacionDetailDTO(update.getId(),
                update.getDescripcion(), update.getFecha(), update.getActivo(),
                update.getMascota().getNombre(), update.getMascota().getTipoMascota().name(),
                update.getMiembro().getId(), update.getMiembro().getNombre(),
                update.getUbicacion().getDireccion(), update.getUbicacion().getAltura(),
                update.getUbicacion().getCiudad(), update.getUbicacion().getRegion(),
                update.getUbicacion().getPais());
    }

    // BAJA LOGICA
    public void eliminarPublicacion (Long id) {

        // Eliminacion regular
        //if (!publicacionRepository.existsById(id)) {
        //      throw new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id);
        //}
        //publicacionRepository.deleteById(id);

        // Eliminacion logica de la Publicacion
        Publicacion p = publicacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontro la Publicacion con el ID = "+id));
        p.setActivo(false);
        // SI la ubicacion de la Publicacion no es nula
        if (p.getUbicacion() != null) {
            // Baja lógica de la ubicación asociada
            p.getUbicacion().setActivo(false);
        }
        // SE ACTUALIZA REALIZANDO SU BAJA LOGICA
        publicacionRepository.save(p);
    }

    // FUNCION AUXILIAR QUE RETORNA 'TRUE' EN CASO DE QUE LA UBICACION ENVIADA EXISTA REALMENTE
    public boolean validarUbicacion(UbicacionRequestDTO ubicacionDTO) {

        // Arma una cadena (ej: "Rivadavia 3470, Mar del Plata, Buenos Aires, Argentina")
        String query = String.format("%s %s, %s, %s, %s",
                ubicacionDTO.direccion(),
                ubicacionDTO.altura(),
                ubicacionDTO.ciudad(),
                ubicacionDTO.region(),
                ubicacionDTO.pais()
        );

        // Construye la URL para consultar al servicio Nominatim (OpenStreetMap)
        String url = UriComponentsBuilder
                .fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", query)
                .queryParam("format", "json")
                .build()
                .toUriString();

        // Crea un cliente para hacer la petición HTTP
        RestTemplate restTemplate = new RestTemplate();

        // Configura la cabecera "User-Agent" (es obligatoria para que el servidor acepte la petición)
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "mi-app");

        // Crea una entidad HTTP con las cabeceras preparadas
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Hace la consulta (GET) a la URL con las cabeceras
        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class
        );

        // La función devuelve true si:
        // - La respuesta fue exitosa (status 200)
        // - El cuerpo de la respuesta no es nulo
        // - La respuesta no está vacía (es decir, encontró al menos una ubicación)
        return response.getStatusCode() == HttpStatus.OK &&
                response.getBody() != null &&
                !response.getBody().equals("[]");
    }


}
