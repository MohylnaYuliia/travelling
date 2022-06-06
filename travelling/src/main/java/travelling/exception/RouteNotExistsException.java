package travelling.exception;

public class RouteNotExistsException extends RuntimeException {
    public RouteNotExistsException(String message) {
        super(message);
    }
}
