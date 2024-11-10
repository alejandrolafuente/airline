package bantads.airline.exceptions;

public class NoFlightNotFoundException extends RuntimeException {

    public NoFlightNotFoundException(String msg) {
        super(msg);
    }
}
