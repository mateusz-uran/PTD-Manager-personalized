package io.github.mateuszuran.ptdmanagerpersonalized.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorMessage> userNotFoundException(UserExistsException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordChangedException.class)
    public ResponseEntity<ErrorMessage> passwordChangeException(PasswordChangedException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorMessage> roleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorMessage> vehicleNotFoundException(VehicleNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        if (ex.getMessage().contains("Vehicle with given id: ")) {
            message.setStatusCode(HttpStatus.NOT_FOUND.value());
            message.setTimestamp(ErrorMessage.trimExceptionTimestamp());
            message.setDescription(ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message = new ErrorMessage(
                    HttpStatus.CONFLICT.value(),
                    ErrorMessage.trimExceptionTimestamp(),
                    ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ErrorMessage> fileUploadException(FileUploadException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        if (ex.getMessage().contains("File not found, please try again.")) {
            message.setStatusCode(HttpStatus.CONFLICT.value());
            message.setTimestamp(ErrorMessage.trimExceptionTimestamp());
            message.setDescription(ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else if (ex.getMessage().contains("Invalid file type: ")) {
            message = new ErrorMessage(
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                    ErrorMessage.trimExceptionTimestamp(),
                    ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else {
            message = new ErrorMessage(
                    HttpStatus.BAD_REQUEST.value(),
                    ErrorMessage.trimExceptionTimestamp(),
                    ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(CardExceptions.class)
    public ResponseEntity<ErrorMessage> cardException(CardExceptions ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        if (ex.getMessage().contains("Card with given id")) {
            message.setStatusCode(HttpStatus.NOT_FOUND.value());
            message.setTimestamp(ErrorMessage.trimExceptionTimestamp());
            message.setDescription(ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            message = new ErrorMessage(
                    HttpStatus.CONFLICT.value(),
                    ErrorMessage.trimExceptionTimestamp(),
                    ex.getMessage());
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<ErrorMessage> tripNotFoundException(TripNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FuelNotFoundException.class)
    public ResponseEntity<ErrorMessage> fuelNotFoundException(FuelNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CountersNotFoundException.class)
    public ResponseEntity<ErrorMessage> countersNotFoundException(CountersNotFoundException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                ErrorMessage.trimExceptionTimestamp(),
                ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    static class ErrorMessage {
        private int statusCode;
        private String timestamp;
        private String description;

        static String trimExceptionTimestamp() {
            var result = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return result.format(formatter);
        }
    }
}
