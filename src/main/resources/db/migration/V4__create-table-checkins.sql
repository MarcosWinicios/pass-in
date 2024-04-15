CREATE TABLE check_ins (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    attendees_events_id INTEGER NOT NULL,
    FOREIGN KEY (attendees_events_id) REFERENCES attendees_events (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
);