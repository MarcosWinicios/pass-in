package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.dto.attendee.AttendeeEventItemDTO;
import studies.com.passin.projections.AttendeeMinProjection;

import java.util.List;

public interface AttendeeEventRepository extends JpaRepository<AttendeeEvent, Integer> {

    @Query(nativeQuery = true, value =
            "SELECT " +
                "att.id AS \"attendeeId\", " +
                "att.name AS \"attendeeName\", " +
                "att.email AS \"attendeeEmail\" " +
            "FROM " +
                "attendees att " +
                    "INNER JOIN attendees_events atev " +
                "ON att.id = atev.id_attendee " +
            "WHERE " +
                    "atev.id_event = :eventId")
    List<AttendeeMinProjection> findAttendeeByEventId(String eventId);
}
