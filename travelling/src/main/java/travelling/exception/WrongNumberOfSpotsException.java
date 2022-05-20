package travelling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class WrongNumberOfSpotsException extends RuntimeException{
    public WrongNumberOfSpotsException(String message) {
        super(message);
    }

}
