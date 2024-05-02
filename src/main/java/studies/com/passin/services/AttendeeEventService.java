package studies.com.passin.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendee.exceptions.AttendeeNotFoundException;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.domain.event.Event;
import studies.com.passin.projections.AttendeeEventBadgeProjection;
import studies.com.passin.projections.EventAttendeeProjection;
import studies.com.passin.repositories.AttendeeEventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeEventService {

    private final AttendeeEventRepository attendeeEventRepository;

    public AttendeeEvent registerAttendeeOnEvent(Event event, Attendee attendee) {
        AttendeeEvent newEventAttendee = new AttendeeEvent();

        newEventAttendee.setEvent(event);
        newEventAttendee.setAttendee(attendee);
        newEventAttendee.setCreatedAt(LocalDateTime.now());

        this.attendeeEventRepository.save(newEventAttendee);

        return newEventAttendee;
    }

    public AttendeeEvent getEventAttendee(Event event, Attendee attendee) {
        Optional<EventAttendeeProjection> attendeeEvent = this.attendeeEventRepository.findEventIdAndAttendeeId(event.getId(), attendee.getId());

        if(attendeeEvent.isEmpty()){
            throw new AttendeeNotFoundException("Attendee: " + attendee.getId() + " not found at event: " + event.getId());
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

    public void removeAttendeeFromEvent(AttendeeEvent attendeeEvent){
        this.attendeeEventRepository.delete(attendeeEvent);
    }


    public LocalDateTime localDateTimeOfString(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // Convertendo a string para LocalDateTime usando o formato personalizado
        LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);

        System.out.println("LocalDateTime: " + localDateTime);

        return localDateTime;
    }

    public AttendeeEventBadgeProjection getAttendeeEventBadgeProjection(AttendeeEvent attendeeEvent){
        Optional<AttendeeEventBadgeProjection> badge = this.attendeeEventRepository.findAttendeeEventBadge(attendeeEvent.getId());
        if(!badge.isPresent()){
            throw new RuntimeException("Badge not found!");
        }
        return badge.get();
    }


}
