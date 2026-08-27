package pet_finder.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<MensajeDetailDTO> obtenerConversacion(Long idUsuario, Long idOtro, Long desdeId) {
        miembroValidation.validarExistenciaPorId(idOtro);

        List<Mensaje> mensajes = mensajeRepository.findConversacionDesde(idUsuario, idOtro, desdeId);

        return mensajeMapper.deEntidadesAdetails(mensajes);
    }

    @Transactional
    public void marcarLeidos(Long idUsuario, Long idOtro) {
        mensajeRepository.findByReceptorIdAndEmisorIdAndLeidoFalse(idUsuario, idOtro)
                .forEach(m -> m.setLeido(true));
    }

    @Transactional(readOnly = true)
    public List<ConversacionDetailDTO> listarConversaciones(Long idUsuario) {
        Set<Long> idsContactos = new HashSet<>();
        idsContactos.addAll(mensajeRepository.findIdsReceptores(idUsuario));
        idsContactos.addAll(mensajeRepository.findIdsEmisores(idUsuario));

        List<Miembro> contactos = miembroRepository.findAllById(idsContactos);

        // Una query para todos los conteos, en vez de una por contacto.
        // Solo devuelve filas de contactos con mensajes pendientes.
        Map<Long, Long> noLeidosPorContacto = mensajeRepository.contarMensajesNoLeidosPorContacto(idUsuario)
                .stream()
                .collect(Collectors.toMap(
                        fila -> (Long) fila[0],
                        fila -> (Long) fila[1]
                ));

        // Ídem para el último mensaje. fila = [idContacto, texto, fechaEnvio]
        Map<Long, Object[]> ultimoPorContacto = mensajeRepository.findUltimoMensajePorContacto(idUsuario)
                .stream()
                .collect(Collectors.toMap(
                        fila -> (Long) fila[0],
                        fila -> fila
                ));

        return contactos.stream()
                .map(contacto -> {
                    Object[] ultimo = ultimoPorContacto.get(contacto.getId());
                    return new ConversacionDetailDTO(
                            contacto.getId(),
                            contacto.getNombre(),
                            contacto.getApellido(),
                            contacto.isActivo(),
                            noLeidosPorContacto.getOrDefault(contacto.getId(), 0L),
                            ultimo != null ? (String) ultimo[1] : null,
                            ultimo != null ? (LocalDateTime) ultimo[2] : null
                    );
                })
                // Más reciente primero. nullsLast cubre contactos sin mensajes.
                .sorted(Comparator.comparing(
                        ConversacionDetailDTO::fechaUltimoMensaje,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
