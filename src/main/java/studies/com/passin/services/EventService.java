package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.event.Event;
import studies.com.passin.dto.event.EventResponseDTO;
import studies.com.passin.repositories.AttendeeRepository;
import studies.com.passin.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeRepository ateAttendeeRepository;

    public EventResponseDTO getEventDetail(String eventId){

        Event event = this.eventRepository.findById(eventId).orElseThrow(
                () -> new RuntimeException("Event not found with Id: " + eventId));

        List<Attendee> attendeeList = this.ateAttendeeRepository.findByAttendeeId(eventId);

        return new EventResponseDTO(event, attendeeList.size());
    }
}
