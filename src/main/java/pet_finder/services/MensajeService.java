package pet_finder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pet_finder.dtos.mensaje.ConversacionDetailDTO;
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

    public MensajeService(MensajeRepository mensajeRepository, MiembroRepository miembroRepository, MensajeValidation mensajeValidation, MiembroValidation miembroValidation) {
        this.mensajeRepository = mensajeRepository;
        this.miembroRepository = miembroRepository;
        this.mensajeValidation = mensajeValidation;
        this.miembroValidation = miembroValidation;
    }

    public Mensaje enviarMensaje(Mensaje mensaje, Long idEmisor, Long idReceptor) {
        mensajeValidation.validarNoAutoMensaje(idEmisor, idReceptor);

        Miembro emisor = miembroValidation.validarExistenciaPorId(idEmisor);
        Miembro receptor = mensajeValidation.validarReceptorExiste(idReceptor);
        mensajeValidation.validarReceptorActivo(receptor);

        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);

        return mensajeRepository.save(mensaje);
    }

    @Transactional
    public List<Mensaje> obtenerConversacion(Long idUsuario, Long idOtro) {
        miembroValidation.validarExistenciaPorId(idOtro);

        List<Mensaje> mensajes = mensajeRepository.findConversacion(idUsuario, idOtro);

        mensajes.stream()
                .filter(m -> m.getReceptor().getId().equals(idUsuario) && !m.getLeido())
                .forEach(m -> m.setLeido(true));
        mensajeRepository.saveAll(mensajes);

        return mensajes;
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
