package studies.com.passin.domain.attendeeEvent.exceptions;

public class EventAttendeeAlreadyExistsException extends RuntimeException{

    public EventAttendeeAlreadyExistsException(String message){
        super(message);
    }
}
