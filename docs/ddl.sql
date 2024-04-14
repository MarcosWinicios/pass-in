CREATE TABLE events (
    id TEXT NOT NULL PRIMARY KEY,
    title TEXT NOT NULL,
    details TEXT,
    slug TEXT NOT NULL,
    maximum_attendees INTEGER NOT NULL
);

CREATE TABLE attendees (
    id TEXT NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attendees_events (
    id SERIAL NOT NULL PRIMARY KEY,
    id_event TEXT NOT NULL,
    id_attendee TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_event) REFERENCES events(id)
		ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_attendee) REFERENCES attendees(id)
		ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE check_ins (
    id SERIAL NOT NULL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    attendees_events_id INTEGER NOT NULL,
    FOREIGN KEY (attendees_events_id) REFERENCES attendees_events (id)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE UNIQUE INDEX events_slug_key ON events(slug);

CREATE UNIQUE INDEX check_ins_attendee_id_key ON check_ins(attendees_events_id);