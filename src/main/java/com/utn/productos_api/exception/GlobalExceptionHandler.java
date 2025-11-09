package com.utn.productos_api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarProductoNotFound(ProductoNotFoundException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(HttpStatus.NOT_FOUND.value());
        body.setErrorMessage(ex.getMessage());
        body.setRoute(path);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(StockInsuficienteException.class) // <-- nombre correcto
    public ResponseEntity<ErrorResponse> manejarStock(StockInsuficienteException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        body.setErrorMessage(ex.getMessage());
        body.setRoute(path);
        return ResponseEntity.badRequest().body(body);
    }

    // @Valid en el BODY (DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> datosNoValidos(MethodArgumentNotValidException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        Map<String,String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> String.valueOf(fe.getDefaultMessage()),
                        (a,b) -> a
                ));
        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "errors", errors,
                "path", path
        ));
    }

    // @Min, @NotNull, etc. en @PathVariable / @RequestParam (requiere @Validated en el controller)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolation(ConstraintViolationException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        body.setErrorMessage("Parámetros inválidos: " + ex.getMessage());
        body.setRoute(path);
        return ResponseEntity.badRequest().body(body);
    }

    // Tipo inválido en path/query (p.ej., Categoria mal escrita)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> typeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(HttpStatus.BAD_REQUEST.value());
        body.setErrorMessage("Parámetro inválido: " + ex.getName());
        body.setRoute(path);
        return ResponseEntity.badRequest().body(body);
    }

    // Respetar ResponseStatusException (404, 400, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> manejarRSE(ResponseStatusException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(ex.getStatusCode().value());
        body.setErrorMessage(ex.getReason());
        body.setRoute(path);
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    // Fallback 500 (sin pisar RSE)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarExcepciones(Exception ex, WebRequest request) {
        if (ex instanceof ResponseStatusException rse) throw rse;
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ErrorResponse body = new ErrorResponse();
        body.setTimestamp(LocalDateTime.now());
        body.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.setErrorMessage("Error interno del servidor");
        body.setRoute(path);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
