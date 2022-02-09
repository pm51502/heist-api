package heist.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class HeistReadyException extends RuntimeException{
    public HeistReadyException(String message) {
        super(message);
    }
}
