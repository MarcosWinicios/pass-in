package studies.com.passin.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import studies.com.passin.dto.attendee.*;
import studies.com.passin.dto.event.EventDetailDTO;
import studies.com.passin.dto.event.EventIdDTO;
import studies.com.passin.dto.event.EventRequestDTO;
import studies.com.passin.dto.event.EventResponseDTO;
import studies.com.passin.services.AttendeeService;
import studies.com.passin.services.EventService;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}")
                .buildAndExpand(eventIdDTO.eventId())
                .toUri();

        return  ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping
    public  ResponseEntity<List<EventDetailDTO>> getAllEvent(){
        var response  = this.eventService.getAllEvents();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId){
        EventResponseDTO event = this.eventService.getEventDetail(eventId);
        return ResponseEntity.ok(event);

    }

    @GetMapping("/{eventId}/attendees")
    public ResponseEntity<List<AttendeeEventItemDTO>> getEventAttendees(@PathVariable String eventId){
        List<AttendeeEventItemDTO> response = this.eventService.getEventAttendees(eventId);
        return ResponseEntity.ok(response);
    }
/*
    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeToEventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId,body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge")
                .buildAndExpand(attendeeIdDTO.attendeeId())
                .toUri();

        return  ResponseEntity.created(uri).body(attendeeIdDTO);
    }*/

/*
    private final EventService eventService;
    private final AttendeeService attendeeService;


    @GetMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String eventId){
        AttendeesListResponseDTO attendeesListResponseDTO = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok(attendeesListResponseDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId,body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge")
                .buildAndExpand(attendeeIdDTO.attendeeId())
                .toUri();

        return  ResponseEntity.created(uri).body(attendeeIdDTO);
    }


 */
}