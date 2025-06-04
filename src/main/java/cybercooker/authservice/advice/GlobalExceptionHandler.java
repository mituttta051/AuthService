package cybercooker.authservice.advice;


import cybercooker.authservice.exception.BaseException;
import cybercooker.authservice.exception.NotValidRequestException;
import cybercooker.authservice.exception.details.NotValidRequestDetails;
import cybercooker.authservice.mapper.ErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseException> serviceExceptionHandler(BaseException e) {
        return ErrorMapper.map(e);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<NotValidRequestException> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotValidRequestException(new NotValidRequestDetails(Map.of("parameter", "Invalid " + ex.getRequiredType().getSimpleName() + " format: " + ex.getValue()))));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NotValidRequestException> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotValidRequestException(new NotValidRequestDetails(errors)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<NotValidRequestException> handleIllegalArgumentExceptions(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new NotValidRequestException(new NotValidRequestDetails(errors)));
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, String> error = Map.of("error", "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalServerErrorHandler(Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }
}