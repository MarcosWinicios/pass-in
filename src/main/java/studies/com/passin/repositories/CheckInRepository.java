package studies.com.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import studies.com.passin.domain.checkIn.CheckIn;

public interface CheckInRepository extends JpaRepository<CheckIn, Integer> {
}
