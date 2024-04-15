package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendee.exceptions.EmailAlreadyInUseException;
import studies.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import studies.com.passin.domain.checkIn.CheckIn;
import studies.com.passin.dto.attendee.*;
import studies.com.passin.repositories.AttendeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;


/*

    private final CheckInService checkInService;

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


    public void deleteAttendee(String attendId){
        this.checkInService.deleteCheckIn(attendId);
        this.attendeeRepository.delete(this.getAttendee(attendId));
    }

    public void checkInAttendee(String attendeeId) {
        Attendee attendee = this.getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }

    private Attendee getAttendee(String attendeeId){
       return this.attendeeRepository
                .findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
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

    public List<AttendeeDetailsDTO> getAllAttendees(){
        List<Attendee> attendeeList = this.attendeeRepository.findAll();

        return attendeeList.stream().map(attendee -> new AttendeeDetailsDTO(
                attendee.getId(),
                attendee.getName(),
                attendee.getEmail(),
                attendee.getCreatedAt()
        )).toList();

    }
    public AttendeeDetailsDTO getAttendeeDetails(String attendeeId){
        Attendee attendee = this.getAttendee(attendeeId);

         return new AttendeeDetailsDTO(
                attendee.getId(),
                attendee.getName(),
                attendee.getEmail(),
                attendee.getCreatedAt());
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository
                .findById(attendeeId)
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }


    public AttendeeIdDTO registerAttendee(AttendeeRequestDTO attendeeRequestDTO){
        this.checkEmailDuplicity(attendeeRequestDTO.email());

        Attendee newAttendee = new Attendee();

        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setCreatedAt(LocalDateTime.now());

        System.out.println("Attendee before: " + newAttendee);

        this.attendeeRepository.save(newAttendee);

        System.out.println("Attendee after: " + newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }


    public void checkEmailDuplicity(String email){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEmail(email);

        if(isAttendeeRegistered.isPresent()){
            throw new EmailAlreadyInUseException("Este email já está em uso");
        }

    }


}
