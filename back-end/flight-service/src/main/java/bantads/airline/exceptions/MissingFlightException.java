package bantads.airline.exceptions;

public class MissingFlightException extends RuntimeException {

    public MissingFlightException(String msg) {
        super(msg);
    }

}
