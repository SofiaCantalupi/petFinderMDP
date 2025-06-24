package pet_finder.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pet_finder.exceptions.*;
import pet_finder.exceptions.model.ErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {

    // para excepciones en general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // para errores que surgan de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejadorValidaciones(MethodArgumentNotValidException exc) {
        Map<String, String> errors = exc.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

    }

    // ------ Handlers para excepciones especificas

    // para excepcion EntityNotFound
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarNotFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // EmailYaRegistradoException
    @ExceptionHandler(EmailYaRegistradoException.class)
    public ResponseEntity<ErrorResponse> manejarEmailYaRegistrado(EmailYaRegistradoException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT,exc.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // UsuarioNoEncontradoException
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // FormatoInvalidoException
    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarFormatoInvalido(FormatoInvalidoException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // MiembroInactivoException
    @ExceptionHandler(MiembroInactivoException.class)
    public ResponseEntity<ErrorResponse> manejarMiembroInactivo(MiembroInactivoException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // EntidadInactivaException
    @ExceptionHandler(EntidadInactivaException.class)
    public ResponseEntity<ErrorResponse> manejarEntidadInactiva(EntidadInactivaException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // UbicacionInvalidaException
    @ExceptionHandler(UbicacionInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarUbicacionInvalida(UbicacionInvalidaException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Operación no permitida Exception
    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<String> manejarOperacionNoPermitidaException(OperacionNoPermitidaException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalArgument(IllegalArgumentException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalArgument(IllegalStateException exc){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, exc.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // ErrorEnRolException
    @ExceptionHandler(ErrorEnRolException.class)
    public ResponseEntity<ErrorResponse> manejarErrorEnRol(ErrorEnRolException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
