CREATE TABLE user_note_permissions (
	username VARCHAR,
	note_id BIGINT,
	readable BOOLEAN,
	writable BOOLEAN
);

CREATE INDEX ON user_note_permissions(username);
CREATE INDEX ON user_note_permissions(username, note_id);
