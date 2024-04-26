package studies.com.passin.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import studies.com.passin.dto.attendee.AttendeeBadgeResponseDTO;
import studies.com.passin.dto.attendee.AttendeeDetailsDTO;
import studies.com.passin.dto.attendee.AttendeeIdDTO;
import studies.com.passin.dto.attendee.AttendeeRequestDTO;
import studies.com.passin.services.AttendeeService;
import studies.com.passin.services.CheckInService;

import java.util.List;

@RestController
@RequestMapping("/attendees")
@RequiredArgsConstructor
public class AttendeeController {

    @Autowired
    private AttendeeService attendeeService;

    @PostMapping
    public ResponseEntity<AttendeeIdDTO> registerAttendee(@RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.attendeeService.registerAttendee(body);

        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge")
                .buildAndExpand(attendeeIdDTO.attendeeId())
                .toUri();

        return  ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    @GetMapping("/{attendeeId}")
    public ResponseEntity<AttendeeDetailsDTO> getAttendeeBadge(@PathVariable String attendeeId){
        AttendeeDetailsDTO response =  this.attendeeService.getAttendeeDetails(attendeeId);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<AttendeeDetailsDTO>> getAllAttendees(){
        List<AttendeeDetailsDTO> response = this.attendeeService.getAllAttendees();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{attendeeId}")
    public ResponseEntity<Void> deleteAttendee(@PathVariable String attendeeId){

        this.attendeeService.deleteAttendee(attendeeId);

        return  ResponseEntity.noContent().build();
    }

}
