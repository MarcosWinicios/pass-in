package studies.com.passin.domain.attendee.exceptions;

public class EmailAlreadyInUseException extends RuntimeException{

    public EmailAlreadyInUseException(String message){
        super(message);
    }
}
