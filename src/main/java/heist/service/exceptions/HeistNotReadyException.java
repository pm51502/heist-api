package heist.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class HeistNotReadyException extends RuntimeException{
    public HeistNotReadyException(String message) {
        super(message);
    }
}
