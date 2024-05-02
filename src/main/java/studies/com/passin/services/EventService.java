package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.domain.attendeeEvent.exceptions.EventAttendeeAlreadyExistsException;
import studies.com.passin.domain.event.Event;
import studies.com.passin.domain.event.exceptions.EventFullException;
import studies.com.passin.domain.event.exceptions.EventNotFoundException;
import studies.com.passin.dto.attendee.*;
import studies.com.passin.dto.event.EventDetailDTO;
import studies.com.passin.dto.event.EventIdDTO;
import studies.com.passin.dto.event.EventRequestDTO;
import studies.com.passin.dto.event.EventResponseDTO;
import studies.com.passin.projections.AttendeeEventBadgeProjection;
import studies.com.passin.repositories.EventRepository;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {


    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;
    private final AttendeeEventService attendeeEventService;
    private final CheckInService checkInService;

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

        AttendeeEvent newEventAttendee = this.attendeeEventService.registerAttendeeOnEvent(event, attendee);

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

    public Event getEventById(String eventId){
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
        this.attendeeEventService.removeAttendeeFromEvent(attendeeEvent);
    }

    public AttendeeEvent getEventAttendee(String eventId, String attendeeId){

        var event = this.getEventById(eventId);
        var attendee = this.attendeeService.getAttendee(attendeeId);

        return this.attendeeEventService.getEventAttendee(event, attendee);
    }

    public void checkInAttendee(String eventId, String attendeeId) {
        AttendeeEvent attendeeEvent = this.getEventAttendee(eventId, attendeeId);

        this.checkInService.registerCheckIn(attendeeEvent);
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String eventId, String attendeeId, UriComponentsBuilder uriComponentsBuilder) {
        var attendeeRegister = this.getEventAttendee(eventId, attendeeId);

        AttendeeEventBadgeProjection badgeProjection =  this.attendeeEventService.getAttendeeEventBadgeProjection(attendeeRegister);

        var uri = uriComponentsBuilder.path("events/{eventId}/check-in/{attendeeId}")
                .buildAndExpand(badgeProjection.getEventId(), badgeProjection.getAttendeeId())
                .toUri()
                .toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(
                badgeProjection.getAttendeeName(),
                badgeProjection.getAttendeeEmail(),
                uri,
                badgeProjection.getEventId());

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);

    }
}
