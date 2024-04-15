package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studies.com.passin.domain.attendee.Attendee;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {

//    List<Attendee> findByEventId(String eventId);
//
//    Optional<Attendee> findByEventIdAndEmail(String eventId, String email);
}
