package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendee.exceptions.AttendeeAlreadyExistException;
import studies.com.passin.domain.checkIn.CheckIn;
import studies.com.passin.dto.attendee.AttendeeDetails;
import studies.com.passin.dto.attendee.AttendeesListResponseDTO;
import studies.com.passin.repositories.AttendeeRepository;
import studies.com.passin.repositories.CheckInRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInRepository checkInRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkedInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetails(
                    attendee.getId(),
                    attendee.getName(),
                    attendee.getEmail(),
                    attendee.getCreatedAt(),
                    checkedInAt
                    );
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }


    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);

        if(isAttendeeRegistered.isPresent()){
            throw new AttendeeAlreadyExistException("Attendee is already registered");
        }

    }

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);

        return newAttendee;
    }
}
