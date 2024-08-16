package backend.microservices.gatewayservice.exception.handler;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private HttpStatus httpStatus;
    private String exceptionName;
    private String message;

    public ExceptionResponse(HttpStatus httpStatus, String exceptionName, String message) {
        this.httpStatus = httpStatus;
        this.exceptionName = exceptionName;
        this.message = message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
