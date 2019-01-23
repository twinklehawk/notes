SET search_path TO ${schema};

CREATE USER ${username} PASSWORD '${password}';
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA ${schema} TO ${username};

CREATE TABLE notes (
	id BIGSERIAL PRIMARY KEY,
	correlation_id BIGINT,
	title VARCHAR(128),
	content VARCHAR(4096)
);

CREATE TABLE user_note_permissions (
	username VARCHAR NOT NULL,
	note_id BIGINT NOT NULL,
	readable BOOLEAN NOT NULL,
	writable BOOLEAN NOT NULL,
	owner BOOLEAN NOT NULL
);
CREATE INDEX ON user_note_permissions(username);
CREATE UNIQUE INDEX ON user_note_permissions(username, note_id);
