package travelling.exception;

public class SpotsSoldOutException extends RuntimeException {
    public SpotsSoldOutException(String message) {
        super(message);
    }
}
