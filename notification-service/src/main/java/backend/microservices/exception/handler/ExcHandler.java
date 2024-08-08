package backend.microservices.exception.handler;

import backend.microservices.exception.SendMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExcHandler {

    @ExceptionHandler(SendMessageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse notFoundException(SendMessageException e) {
        return new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getClass().getName(), e.getMessage());
    }
}
