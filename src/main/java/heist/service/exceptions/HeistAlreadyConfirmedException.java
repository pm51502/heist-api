package heist.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class HeistAlreadyConfirmedException extends RuntimeException{
    public HeistAlreadyConfirmedException(String message) {
        super(message);
    }
}
