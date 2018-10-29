ALTER TABLE talks DROP COLUMN topic;
ALTER TABLE talks ADD topic_id UUID;

CREATE TABLE topics (
  id UUID NOT NULL PRIMARY KEY,
  topic VARCHAR(255) NOT NULL
);

ALTER TABLE talks
ADD CONSTRAINT talks_topics_id_fk
FOREIGN KEY (topic_id) REFERENCES topics(id);