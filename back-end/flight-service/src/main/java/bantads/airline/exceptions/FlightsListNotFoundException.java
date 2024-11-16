package bantads.airline.exceptions;

public class FlightsListNotFoundException extends RuntimeException {
    
    public FlightsListNotFoundException(String msg) {
        super(msg);
    }
}
