package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.domain.attendeeEvent.exceptions.EventAttendeeAlreadyExistsException;
import studies.com.passin.domain.event.Event;
import studies.com.passin.domain.event.exceptions.EventFullException;
import studies.com.passin.dto.attendee.AttendeeEventItemDTO;
import studies.com.passin.dto.attendee.AttendeeIdDTO;
import studies.com.passin.dto.attendee.AttendeeToEventRequestDTO;
import studies.com.passin.dto.attendee.EventAttendeeRegisteredDTO;
import studies.com.passin.dto.event.EventDetailDTO;
import studies.com.passin.dto.event.EventIdDTO;
import studies.com.passin.dto.event.EventRequestDTO;
import studies.com.passin.dto.event.EventResponseDTO;
import studies.com.passin.domain.event.exceptions.EventNotFoundException;
import studies.com.passin.projections.AttendeeMinProjection;
import studies.com.passin.projections.EventAttendeeProjection;
import studies.com.passin.repositories.AttendeeEventRepository;
import studies.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {


    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;
    private final AttendeeEventRepository attendeeEventRepository;

    public EventIdDTO createEvent(EventRequestDTO eventDTO){
        Event newEvent =  new Event();
        newEvent.setTitle(eventDTO.title());
        newEvent.setDetails(eventDTO.details());
        newEvent.setMaximumAttendees(eventDTO.maximumAttendees());
        newEvent.setSlug(this.createSlug(eventDTO.title()));

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

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
                        this.getEventAttendees(event.getId()).size()
                ))
                .toList();
    }

    public EventResponseDTO getEventDetail(String eventId){

        Event event = this.getEventById(eventId);

        var attendeeList = this.attendeeService.getAttendeesByEventId(event.getId());

        return new EventResponseDTO(event, attendeeList.size());
    }

    public EventAttendeeRegisteredDTO registerAttendeeOnEvent(String eventId, AttendeeToEventRequestDTO attendeeId){

        Event event = this.getEventById(eventId);
        Attendee attendee = this.attendeeService.getAttendee(attendeeId.attendeeId());

        List<AttendeeEventItemDTO> attendeeList = this.checkEventAttendee(event.getId(), attendee);

        if(event.getMaximumAttendees() <= attendeeList.size()){
            throw new EventFullException("Event is Full");
        }

        AttendeeEvent newEventAttendee = new AttendeeEvent();

        newEventAttendee.setEvent(event);
        newEventAttendee.setAttendee(attendee);
        newEventAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeEventRepository.save(newEventAttendee);

        return new EventAttendeeRegisteredDTO(newEventAttendee.getId());
    }

    public List<AttendeeEventItemDTO> getEventAttendees(String eventId){
        return this.attendeeService
                .getAttendeesByEventId(this.getEventById(eventId).getId());
    }

    private List<AttendeeEventItemDTO> checkEventAttendee(String eventId, Attendee attendee){
        var attendeeList = this.getEventAttendees(eventId);
        attendeeList.forEach( x -> {
            if(x.attendeeId().equals(attendee.getId())){
                throw new EventAttendeeAlreadyExistsException("Attendee " + x.attendeeId() + " is already registered for this event");
            }
        });

        return attendeeList;
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

    public void removeAttendee(String eventId, String attendeeId) {

        AttendeeEvent attendeeEvent = this.getEventAttendee(eventId, attendeeId);

        System.out.println(attendeeEvent.toString());

        this.attendeeEventRepository.delete(attendeeEvent);
    }

    public AttendeeEvent getEventAttendee(String eventId, String attendeeId){

        var event = this.getEventById(eventId);
        var attendee = this.attendeeService.getAttendee(attendeeId);

        Optional<EventAttendeeProjection> attendeeEvent = this.attendeeEventRepository.findEventIdAndAttendeeId(eventId, attendeeId);

        if(attendeeEvent.isEmpty()){
            throw new AttendeeNotFoundException("Attendee: " + attendeeId + " not found at event: " + eventId);
        }

        event.setId(attendeeEvent.get().getEventId());
        attendee.setId(attendeeEvent.get().getAttendeeId());

        return new AttendeeEvent(
                Integer.parseInt(attendeeEvent.get().getId()),
                event,
                attendee,
                this.localDateTimeOfString(attendeeEvent.get().getCreatedAt())
        );
    }

    public LocalDateTime localDateTimeOfString(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // Convertendo a string para LocalDateTime usando o formato personalizado
        LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);

        System.out.println("LocalDateTime: " + localDateTime);

        return localDateTime;
    }
}
