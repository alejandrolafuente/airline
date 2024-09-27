package bantads.airline.exceptions;

public class FlightsNotFoundException extends RuntimeException {

    public FlightsNotFoundException() {
        super("Cannot find flights");
    }

    public FlightsNotFoundException(String message) {
        super(message);
    }

}
