package pet_finder.validations;

import org.springframework.stereotype.Component;
import pet_finder.exceptions.OperacionNoPermitidaException;
import pet_finder.exceptions.UsuarioNoEncontradoException;
import pet_finder.models.Miembro;
import pet_finder.repositories.MiembroRepository;

@Component
public class MensajeValidation {

    private final MiembroRepository miembroRepository;

    public MensajeValidation(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    public Miembro validarReceptorExiste(Long idReceptor) {
        return miembroRepository.findById(idReceptor)
                .orElseThrow(() -> new UsuarioNoEncontradoException("No se encontró el usuario receptor con ID: " + idReceptor));
    }

    public void validarNoAutoMensaje(Long idEmisor, Long idReceptor) {
        if (idEmisor.equals(idReceptor)) {
            throw new OperacionNoPermitidaException("No podés enviarte mensajes a vos mismo");
        }
    }

    public void validarReceptorActivo(Miembro receptor) {
        if (!receptor.isActivo()) {
            throw new OperacionNoPermitidaException("No se puede enviar mensajes a un usuario inactivo");
        }
    }
}
