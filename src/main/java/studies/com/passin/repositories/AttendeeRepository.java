package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studies.com.passin.domain.attendee.Attendee;

public interface AttendeeRepository extends JpaRepository<Attendee, String> {
}
