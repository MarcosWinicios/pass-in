package studies.com.passin.domain.attendeeEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import studies.com.passin.domain.attendee.Attendee;
import studies.com.passin.domain.event.Event;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendee_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_event",
            referencedColumnName = "id",
            nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_attendee",
            referencedColumnName = "id",
            nullable = false)
    private Attendee attendee;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
