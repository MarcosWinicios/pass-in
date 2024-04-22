package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.domain.checkIn.CheckIn;
import studies.com.passin.domain.checkIn.exceptions.CheckInAlreadyExistsException;
import studies.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;

    public void registerCheckIn(AttendeeEvent attendeeEvent){

        this.verifyCheckInExists(attendeeEvent.getId());

        CheckIn newCheckIn = new CheckIn();
        newCheckIn.setAttendeeEvent(attendeeEvent);
        newCheckIn.setCreatedAt(LocalDateTime.now());

        this.checkInRepository.save(newCheckIn);
    }


    private void verifyCheckInExists(Integer attendeeId) {
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);

        if(isCheckedIn.isPresent()){
            throw  new CheckInAlreadyExistsException("Attendee already checked in");
        }

    }

    public Optional<CheckIn> getCheckIn(Integer attendeeEventId) {
        return this.checkInRepository.findByAttendeeEventId(attendeeEventId);
    }
/*
    public void deleteCheckIn(String attendeeId){
        Optional<CheckIn> checkIn = this.getCheckIn(attendeeId);

        if(checkIn.isPresent()){
            this.checkInRepository.delete(checkIn.get());
        }

    }

*/
}
