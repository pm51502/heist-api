package heist.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HeistNotFoundException extends RuntimeException{
    public HeistNotFoundException(String message) {
        super(message);
    }
}
