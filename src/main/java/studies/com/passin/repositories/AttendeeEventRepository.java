package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;
import studies.com.passin.projections.AttendeeMinProjection;
import studies.com.passin.projections.EventAttendeeProjection;

import java.util.List;
import java.util.Optional;

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

    @Query(nativeQuery = true, value =
            "SELECT\n" +
                "    id AS id,\n" +
                "    id_event AS eventId,\n" +
                "    id_attendee AS attendeeId,\n" +
                "    created_at AS createdAt\n" +
            "FROM attendees_events\n" +
            "WHERE " +
                "id_event = :eventId " +
                "AND id_attendee = :attendeeId")
    Optional<EventAttendeeProjection> findEventIdAndAttendeeId(String eventId, String attendeeId);
}
