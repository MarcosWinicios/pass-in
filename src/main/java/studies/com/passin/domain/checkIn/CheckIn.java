package studies.com.passin.domain.checkIn;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.attendeeEvent.AttendeeEvent;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "attendees_events_id",
            referencedColumnName = "id",
            nullable = false)
    private AttendeeEvent attendeeEvent;
}
