package travelling.exception;

public class NoReservationExists extends RuntimeException {
    public NoReservationExists(String message) {
        super(message);
    }
}
