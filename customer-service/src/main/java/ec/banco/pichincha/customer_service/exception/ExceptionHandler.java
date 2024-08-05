package ec.banco.pichincha.customer_service.exception;

import ec.banco.pichincha.customer_service.exception.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomerIdentificationFoundException.class)
    public ResponseEntity<ErrorMessage> handleCustomerIdentificationFoundException(CustomerIdentificationFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomerIdentificationInvalidException.class)
    public ResponseEntity<ErrorMessage> handleCustomerIdentificationInvalidException(CustomerIdentificationInvalidException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}