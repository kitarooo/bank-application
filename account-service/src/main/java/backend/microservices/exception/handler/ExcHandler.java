package backend.microservices.exception.handler;

import backend.microservices.exception.AccountAlreadyExistException;
import backend.microservices.exception.InsufficientFundException;
import backend.microservices.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExcHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundException(NotFoundException e) {
        return new ExceptionResponse(HttpStatus.NOT_FOUND, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(AccountAlreadyExistException.class)
    @ResponseStatus(HttpStatus.FOUND)
    public ExceptionResponse accountAlreadyExistException(AccountAlreadyExistException e) {
        return new ExceptionResponse(HttpStatus.FOUND, e.getClass().getName(), e.getMessage());
    }

    @ExceptionHandler(InsufficientFundException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ExceptionResponse insufficientFundException(InsufficientFundException e) {
        return new ExceptionResponse(HttpStatus.PAYMENT_REQUIRED, e.getClass().getName(), e.getMessage());
    }

}
