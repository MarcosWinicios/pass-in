package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import studies.com.passin.domain.attendee.exceptions.EmailAlreadyInUseException;
import studies.com.passin.dto.attendee.AttendeeDetailsDTO;
import studies.com.passin.dto.attendee.AttendeeEventItemDTO;
import studies.com.passin.dto.attendee.AttendeeIdDTO;
import studies.com.passin.dto.attendee.AttendeeRequestDTO;
import studies.com.passin.repositories.AttendeeEventRepository;
import studies.com.passin.repositories.AttendeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final AttendeeEventRepository attendeeEventRepository;

    private final AttendeeEventService attendeeEventService;


    public List<AttendeeDetailsDTO> getAllAttendees() {
        List<Attendee> attendeeList = this.attendeeRepository.findAll();

        return attendeeList.stream().map(attendee -> new AttendeeDetailsDTO(
                attendee.getId(),
                attendee.getName(),
                attendee.getEmail(),
                attendee.getCreatedAt()
        )).toList();

    }

    public AttendeeDetailsDTO getAttendeeDetails(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);

        return new AttendeeDetailsDTO(
                attendee.getId(),
                attendee.getName(),
                attendee.getEmail(),
                attendee.getCreatedAt());
    }

    public Attendee getAttendee(String attendeeId) {

        return this.attendeeRepository
                .findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }

    public AttendeeIdDTO registerAttendee(AttendeeRequestDTO attendeeRequestDTO) {
        this.checkEmailDuplicity(attendeeRequestDTO.email());

        Attendee newAttendee = new Attendee();

        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeRepository.save(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    public void checkEmailDuplicity(String email) {
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEmail(email);

        if (isAttendeeRegistered.isPresent()) {
            throw new EmailAlreadyInUseException("Este email já está em uso");
        }

    }

    public void deleteAttendee(String attendId) {
//        this.checkInService.deleteCheckIn(attendId);
        this.attendeeRepository.delete(this.getAttendee(attendId));
    }

    public List<AttendeeEventItemDTO> getAttendeesByEventId(String eventId) {
        return this.attendeeEventRepository
                .findAttendeeByEventId(eventId)
                .stream()
                .map(attendee -> new AttendeeEventItemDTO(
                        attendee.getAttendeeId(),
                        attendee.getAttendeeName(),
                        attendee.getAttendeeEmail()
                ))
                .toList();
    }

    /*

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }


    public AttendeesListResponseDTO getEventsAttendee(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkInService.getCheckIn(attendee.getId());

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

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){

        Attendee attendee = this.getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/check-in")
                .buildAndExpand(attendeeId)
                .toUri()
                .toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }
*/
}
