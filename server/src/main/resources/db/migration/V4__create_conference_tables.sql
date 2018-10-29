CREATE TABLE conferences (
  id UUID NOT NULL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE days (
  id UUID NOT NULL PRIMARY KEY,
  date DATE NOT NULL,
  conference_id UUID,
  CONSTRAINT day_conference_fk
  FOREIGN KEY (conference_id) REFERENCES conferences(id)
);

CREATE TABLE tracks (
  id UUID NOT NULL PRIMARY KEY,
  day_id UUID,
  CONSTRAINT track_day_fk
  FOREIGN KEY (day_id) REFERENCES days(id)
);

CREATE TABLE timeslots (
  id UUID NOT NULL PRIMARY KEY,
  talk_id UUID,
  track_id UUID,
  CONSTRAINT timeslot_track_fk
  FOREIGN KEY (track_id) REFERENCES tracks(id),
  CONSTRAINT timeslot_talk_fk
  FOREIGN KEY (talk_id) REFERENCES talks(id)
);

CREATE TABLE speaker (
  id UUID NOT NULL PRIMARY KEY,
  name varchar(255),
  talk_id UUID,
  CONSTRAINT speaker_talk_fk
  FOREIGN KEY (talk_id) REFERENCES talks(id)
);
