package backend.microservices.gatewayservice.exception.handler;


import backend.microservices.gatewayservice.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExcHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse notFoundException(UnauthorizedException e) {
        return new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getClass().getName(), e.getMessage());
    }
}
