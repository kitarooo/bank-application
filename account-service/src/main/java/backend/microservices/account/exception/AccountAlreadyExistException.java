package backend.microservices.account.exception;

public class AccountAlreadyExistException extends RuntimeException{

    public AccountAlreadyExistException(String message) {
        super(message);
    }
}
