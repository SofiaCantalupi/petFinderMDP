package pet_finder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import pet_finder.dtos.mensaje.ConversacionDetailDTO;
import pet_finder.dtos.mensaje.MensajeDetailDTO;
import pet_finder.dtos.mensaje.MensajeRequestDTO;
import pet_finder.mappers.MensajeMapper;
import pet_finder.models.Mensaje;
import pet_finder.models.Miembro;
import pet_finder.repositories.MensajeRepository;
import pet_finder.repositories.MiembroRepository;
import pet_finder.validations.MensajeValidation;
import pet_finder.validations.MiembroValidation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final MiembroRepository miembroRepository;
    private final MensajeValidation mensajeValidation;
    private final MiembroValidation miembroValidation;
    private final MensajeMapper mensajeMapper;

    public MensajeService(MensajeRepository mensajeRepository, MiembroRepository miembroRepository, MensajeValidation mensajeValidation, MiembroValidation miembroValidation, MensajeMapper mensajeMapper) {
        this.mensajeRepository = mensajeRepository;
        this.miembroRepository = miembroRepository;
        this.mensajeValidation = mensajeValidation;
        this.miembroValidation = miembroValidation;
        this.mensajeMapper = mensajeMapper;
    }

    @Transactional
    public MensajeDetailDTO enviarMensaje(MensajeRequestDTO request, Long idEmisor) {
        Long idReceptor = request.getIdReceptor();
        mensajeValidation.validarNoAutoMensaje(idEmisor, idReceptor);

        Mensaje mensaje = mensajeMapper.aEntidad(request);

        Miembro emisor = miembroValidation.validarExistenciaPorId(idEmisor);
        Miembro receptor = mensajeValidation.validarReceptorExiste(idReceptor);
        mensajeValidation.validarReceptorActivo(receptor);

        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);

        Mensaje enviado = mensajeRepository.save(mensaje);
        return mensajeMapper.aDetail(enviado);
    }

    // En un principio desdeId tiene como default 0, es decir, trae toda la conversacion. Luego Angular va a guardar el id del ultimo mensaje traido desde la bd, para traer a partir de el.
    @Transactional
    public List<MensajeDetailDTO> obtenerConversacion(Long idUsuario, Long idOtro, Long desdeId) {
        miembroValidation.validarExistenciaPorId(idOtro);

        List<Mensaje> mensajes = mensajeRepository.findConversacionDesde(idUsuario, idOtro, desdeId);

        mensajes.stream()
                .filter(m -> m.getReceptor().getId().equals(idUsuario) && !m.getLeido())
                .forEach(m -> m.setLeido(true));
        mensajeRepository.saveAll(mensajes);

        return mensajeMapper.deEntidadesAdetails(mensajes);
    }

    public List<ConversacionDetailDTO> listarConversaciones(Long idUsuario) {
        Set<Long> idsContactos = new HashSet<>();
        idsContactos.addAll(mensajeRepository.findIdsReceptores(idUsuario));
        idsContactos.addAll(mensajeRepository.findIdsEmisores(idUsuario));

        List<Miembro> contactos = miembroRepository.findAllById(idsContactos);

        return contactos.stream()
                .map(contacto -> new ConversacionDetailDTO(
                        contacto.getId(),
                        contacto.getNombre(),
                        contacto.getApellido(),
                        mensajeRepository.countMensajesNoLeidos(idUsuario, contacto.getId())
                ))
                .toList();
    }
}
