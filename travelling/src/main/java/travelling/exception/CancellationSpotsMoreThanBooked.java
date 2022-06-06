package travelling.exception;

public class CancellationSpotsMoreThanBooked extends RuntimeException {
    public CancellationSpotsMoreThanBooked(String message) {
        super(message);
    }
}
