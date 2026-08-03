package pet_finder.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pet_finder.exceptions.*;
import pet_finder.exceptions.model.ErrorResponse;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {
    private final Logger logger = LoggerFactory.getLogger(GlobalHandlerException.class);

    // para excepciones en general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarGeneral(Exception ex) {
        logger.error("Error:", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // para errores que surgan de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejadorValidaciones(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
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

    // Se dispara cuando @PreAuthorize rechaza una operacion por rol insuficiente
    // (AuthorizationDeniedException extiende esta clase). Sin este handler caia en
    // el handler generico de Exception y devolvia 500 en vez de 403.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccessDenied(AccessDeniedException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN, "No tenes permisos para realizar esta operacion.");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Cuerpo de la request malformado o con un valor de enum que no matchea ninguna
    // constante (ej. tipoHogar en minuscula). No se expone ex.getMessage() porque
    // incluye detalle interno de Jackson (nombres de paquete y clase).
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarMensajeIlegible(HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es invalido o tiene un formato incorrecto.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Path/query param que no puede convertirse al tipo esperado (ej. /miembros/abc,
    // donde {id} deberia ser un Long).
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "El parametro '" + ex.getName() + "' tiene un formato invalido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Ninguna ruta mapeada matchea la request (ej. metodo HTTP no soportado en un
    // path existente, o un path que no existe en absoluto).
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "El recurso solicitado no existe.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
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
    public ResponseEntity<ErrorResponse> manejarEmailYaRegistrado(EmailYaRegistradoException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // UsuarioNoEncontradoException
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // FormatoInvalidoException
    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<ErrorResponse> manejarFormatoInvalido(FormatoInvalidoException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // MiembroInactivoException
    @ExceptionHandler(MiembroInactivoException.class)
    public ResponseEntity<ErrorResponse> manejarMiembroInactivo(MiembroInactivoException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // EntidadInactivaException
    @ExceptionHandler(EntidadInactivaException.class)
    public ResponseEntity<ErrorResponse> manejarEntidadInactiva(EntidadInactivaException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // UbicacionInvalidaException
    @ExceptionHandler(UbicacionInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarUbicacionInvalida(UbicacionInvalidaException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

//    // Operación no permitida Exception
    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ErrorResponse> manejarOperacionNoPermitida(OperacionNoPermitidaException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.FORBIDDEN,ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalArgument(IllegalArgumentException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> manejarIllegalArgument(IllegalStateException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // ErrorEnRolException
    @ExceptionHandler(ErrorEnRolException.class)
    public ResponseEntity<ErrorResponse> manejarErrorEnRol(ErrorEnRolException ex){
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
