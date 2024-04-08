package studies.com.passin.dto.event.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException (String message){
        super(message);
    }
}
