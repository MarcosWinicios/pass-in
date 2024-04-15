package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studies.com.passin.domain.checkIn.CheckIn;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

//    Optional<CheckIn> findByAttendeeId(String attendeeId);
}
