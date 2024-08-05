package ec.banco.pichincha.account_service.exception;

import ec.banco.pichincha.account_service.exception.dto.ErrorMessage;
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

    @org.springframework.web.bind.annotation.ExceptionHandler(value = AccountNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleAccountNotFoundException(AccountNotFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = TransactionNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleTransactionNotFoundException(TransactionNotFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BalanceTypeSigNumUnavailableException.class)
    public ResponseEntity<ErrorMessage> handleBalanceTypeSigNumUnavailableException(BalanceTypeSigNumUnavailableException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = InsufficientFoundsException.class)
    public ResponseEntity<ErrorMessage> handleInsufficientFoundsException(InsufficientFoundsException ex) {
        ErrorMessage errorResponse = new ErrorMessage();
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BalanceUnavailableException.class)
    public ResponseEntity<ErrorMessage> handleBalanceUnavailableException(BalanceUnavailableException ex) {
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