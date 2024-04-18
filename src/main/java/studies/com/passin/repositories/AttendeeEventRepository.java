package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;

public interface AttendeeEventRepository extends JpaRepository<Long, AttendeeEvent> {
}
