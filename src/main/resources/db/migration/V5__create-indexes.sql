
CREATE UNIQUE INDEX events_slug_key ON events(slug);

CREATE UNIQUE INDEX check_ins_attendee_id_key ON check_ins(attendees_events_id);