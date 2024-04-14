package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.event.Event;
import studies.com.passin.domain.event.exceptions.EventFullException;
import studies.com.passin.dto.attendee.AttendeeIdDTO;
import studies.com.passin.dto.attendee.AttendeeRequestDTO;
import studies.com.passin.dto.event.EventDetailDTO;
import studies.com.passin.dto.event.EventIdDTO;
import studies.com.passin.dto.event.EventRequestDTO;
import studies.com.passin.dto.event.EventResponseDTO;
import studies.com.passin.domain.event.exceptions.EventNotFoundException;
import studies.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AttendeeService ateAttendeeService;

    public List<EventDetailDTO> getAllEvents(){
        return this.eventRepository
                .findAll()
                .stream()
                .map( event -> new EventDetailDTO(
                        event.getId(),
                        event.getTitle(),
                        event.getDetails(),
                        event.getSlug(),
                        event.getMaximumAttendees(),
                        this.ateAttendeeService.getAllAttendeesFromEvent(event.getId()).size()
                ))
                .toList();
    }

    public EventResponseDTO getEventDetail(String eventId){

        Event event = this.eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException("Event not found with Id: " + eventId));

        List<Attendee> attendeeList = this.ateAttendeeService.getAllAttendeesFromEvent(eventId);

        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent =  new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.ateAttendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.ateAttendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()){
            throw new EventFullException("Event is Full");
        }

        Attendee newAttendee = new Attendee();

        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.ateAttendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException("Event not found with Id: " + eventId));
    }


    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "").
                replaceAll("[^\\w\\s]", "").
                replaceAll("\\s+", "-")
                .toLowerCase();
    }

}
