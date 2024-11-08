package bantads.airline.exceptions;

public class FlightNotFoundException extends RuntimeException {

    public FlightNotFoundException(String msg) {
        super(msg);
    }
}
